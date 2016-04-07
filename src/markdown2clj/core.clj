(ns markdown2clj.core
  (:import [org.commonmark.parser Parser]
           [org.commonmark.html HtmlRenderer]
           [org.commonmark Extension]
           [org.commonmark.ext.gfm.tables TablesExtension TableBlock TableCell
            TableBody TableHead TableRow]
           [org.commonmark.node Block BlockQuote
            BulletList Code CustomBlock CustomNode Document
            Emphasis FencedCodeBlock HardLineBreak Heading
            HtmlBlock HtmlInline Image IndentedCodeBlock Link
            ListBlock ListItem Node OrderedList Paragraph
            SoftLineBreak StrongEmphasis Text ThematicBreak]))

(declare parse)

(defn in?
  "true if coll contains elm"
  [coll elm]
  (some #(= elm %) coll))

(defn get-siblings
  [node]
  (if (nil? node)
    []
    (cons node (get-siblings (.getNext node)))))

(defn get-children
  [parent]
  (get-siblings (.getFirstChild parent)))

(defn table?
  [text]
  (let [table (-> text
                  parse
                  :document
                  first)]
    (when (contains? table :table-block)
      table)))

;; reduces a paragraph with a vector of childern into a
;; string. Used to parse tabbed tables data.
;; TODO: As of now, it just skims through the vector and picks the first
;; layer of `:text`. Should be able pick from nested map to handle
;; `:bold`, `:italic` etc in the future
(defn reduce-paragraph
  [p]
  (reduce (fn [string txt-map]
            (if (contains? txt-map :text)
              (str string (:text txt-map) "\n")
              string))
          ""
          p))

;; Visitor definded as a protocol, that would visit each node
;; and build a clojure map in the process.
(defprotocol Visitor
  (visit [this params]))

(extend-protocol Visitor

  Block
  (visit [this params]
    {:block
     (into [] (for [child (get-children this)]
                (visit child params)))})

  BlockQuote
  (visit [this params]
    {:block-quote
     (into [] (for [child (get-children this)]
                (visit child params)))})

  BulletList
  (visit [this params]
    {:bullet-list
     (into [] (for [child (get-children this)]
                (visit child params)))})

  Code
  (visit [this _]
    {:code
     [{:text (.getLiteral this)}]})

  Document
  (visit [this params]
    {:document
     (into [] (for [child (get-children this)]
                (visit child params)))})

  Emphasis
  (visit [this params]
    {:italic
     (into [] (for [child (get-children this)]
                (visit child params)))})

  FencedCodeBlock
  (visit [this _]
    {:fenced-code-block
     [{:char (.getFenceChar this)}
      {:indentation (.getFenceIndent this)}
      {:length (.getFenceLength this)}
      {:info (.getInfo this)}
      {:text (.getLiteral this)}]})

  HardLineBreak
  (visit [this _]
    {:hard-line-break []})

  Heading
  (visit [this params]
    {:heading
     (into [{:level (.getLevel this)}]
           (for [child (get-children this)]
             (visit child params)))})

  HtmlBlock
  (visit [this _]
    {:html-block
     [:text (.getLiteral this)]})

  HtmlInline
  (visit [this _]
    {:html-inline
     [:text (.getLiteral this)]})

  Image
  (visit [this params]
    {:image
     (into [{:title (.getTitle this)}
            {:destination (.getDestination this)}]
           (for [child (get-children this)]
             (visit child params)))})

  IndentedCodeBlock
  (visit [this _]
    {:indented-code-block
     [{:text (.getLiteral this)}]})

  Link
  (visit [this params]
    {:link
     (into [{:title (.getTitle this)}
            {:destination (.getDestination this)}]
           (for [child (get-children this)]
             (visit child params)))})

  ListItem
  (visit [this params]
    {:list-item
     (into [] (for [child (get-children this)]
                (visit child params)))})

  OrderedList
  (visit [this params]
    {:ordered-list
     (into [] (for [child (get-children this)]
                (visit child params)))})

  Paragraph
  (visit [this params]
    (let [p (into [] (for [child (get-children this)]
                       (visit child params)))]
      (if (in? params :parse-tabbed-tables)
        (let [t (table? (reduce-paragraph p))]
          (or t {:paragraph p}))
        {:paragraph p})))

  StrongEmphasis
  (visit [this params]
    {:bold
     (into [] (for [child (get-children this)]
                (visit child params)))})

  SoftLineBreak
  (visit [this _]
    {:soft-line-break []})

  TableBlock
  (visit [this params]
    {:table-block
     (into [] (for [child (get-children this)]
                (visit child params)))})

  TableHead
  (visit [this params]
    {:table-head
     (into [] (for [child (get-children this)]
                (visit child params)))})

  TableBody
  (visit [this params]
    {:table-body
     (into [] (for [child (get-children this)]
                (visit child params)))})

  TableRow
  (visit [this params]
    {:table-row
     (into [] (for [child (get-children this)]
                (visit child params)))})

  TableCell
  (visit [this params]
    {:table-cell
     (into [] (for [child (get-children this)]
                (visit child params)))})

  Text
  (visit [this _]
    (let [text (.getLiteral this)]
      (if (or (= text "\\pagebreak") (= text "\\newpage"))
        {:page-break []}
        {:text text})))

  ThematicBreak
  (visit [this _]
    {:thematic-break []}))

(defn parse
  "Accepts a markdown string and an optional params and outputs a clojure map
  with elements as keys and vector of children as values. This is based on the
  commonmarkjava library and mostly supports the same markdown specs.

  The following are valid params
   -   `:parse-tabbed-tables` parses tables that are indented -> within a paragraph"
  ([md-string  & params]
   (let [extention (list (TablesExtension/create))
         parser (.build (.extensions (Parser/builder) extention))
         md-document (.parse parser md-string)]
     (visit md-document params))))
