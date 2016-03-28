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

(defn reduce-paragraph
  [p]
  (reduce (fn [string txt-map]
            (if (contains? txt-map :text)
              (str string (:text txt-map) "\n")
              string))
          ""
          p))

(defprotocol Visitor
  (visit [this em-tables]))

(extend-protocol Visitor

  Block
  (visit [this em-tables]
    {:block
     (into [] (for [child (get-children this)]
                (visit child em-tables)))})

  BlockQuote
  (visit [this em-tables]
    {:block-quote
     (into [] (for [child (get-children this)]
                (visit child em-tables)))})

  BulletList
  (visit [this em-tables]
    {:bullet-list
     (into [] (for [child (get-children this)]
                (visit child em-tables)))})

  Code
  (visit [this _]
    {:code
     [{:text (.getLiteral this)}]})

  Document
  (visit [this em-tables]
    {:document
     (into [] (for [child (get-children this)]
                (visit child em-tables)))})

  Emphasis
  (visit [this em-tables]
    {:italic
     (into [] (for [child (get-children this)]
                (visit child em-tables)))})

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
  (visit [this em-tables]
    {:heading
     (into [{:level (.getLevel this)}]
           (for [child (get-children this)]
             (visit child em-tables)))})

  HtmlBlock
  (visit [this _]
    {:html-block
     [:text (.getLiteral this)]})

  HtmlInline
  (visit [this _]
    {:html-inline
     [:text (.getLiteral this)]})

  Image
  (visit [this em-tables]
    {:image
     (into [{:title (.getTitle this)}
            {:destination (.getDestination this)}]
           (for [child (get-children this)]
             (visit child em-tables)))})

  IndentedCodeBlock
  (visit [this _]
    {:indented-code-block
     [{:text (.getLiteral this)}]})

  Link
  (visit [this em-tables]
    {:link
     (into [{:title (.getTitle this)}
            {:destination (.getDestination this)}]
           (for [child (get-children this)]
             (visit child em-tables)))})

  ListItem
  (visit [this em-tables]
    {:list-item
     (into [] (for [child (get-children this)]
                (visit child em-tables)))})

  OrderedList
  (visit [this em-tables]
    {:ordered-list
     (into [] (for [child (get-children this)]
                (visit child em-tables)))})

  Paragraph
  (visit [this em-tables]
    (let [p (into [] (for [child (get-children this)]
                       (visit child em-tables)))]
      (if (true? em-tables)
        (let [t (table? (reduce-paragraph p))]
          (or t {:paragraph p}))
        {:paragraph p})))

  StrongEmphasis
  (visit [this em-tables]
    {:bold
     (into [] (for [child (get-children this)]
                (visit child em-tables)))})

  SoftLineBreak
  (visit [this _]
    {:soft-line-break []})

  TableBlock
  (visit [this em-tables]
    {:table-block
     (into [] (for [child (get-children this)]
                (visit child em-tables)))})

  TableHead
  (visit [this em-tables]
    {:table-head
     (into [] (for [child (get-children this)]
                (visit child em-tables)))})

  TableBody
  (visit [this em-tables]
    {:table-body
     (into [] (for [child (get-children this)]
                (visit child em-tables)))})

  TableRow
  (visit [this em-tables]
    {:table-row
     (into [] (for [child (get-children this)]
                (visit child em-tables)))})

  TableCell
  (visit [this em-tables]
    {:table-cell
     (into [] (for [child (get-children this)]
                (visit child em-tables)))})

  Text
  (visit [this _]
    {:text (.getLiteral this)})

  ThematicBreak
  (visit [this _]
    {:thematic-break []}))

(defn parse
  ([md-string] (parse md-string false))
  ([md-string embeded-tables]
   (let [extention (list (TablesExtension/create))
         parser (.build (.extensions (Parser/builder) extention))
         md-document (.parse parser md-string)]
     (visit md-document embeded-tables))))
