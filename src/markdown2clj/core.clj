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

(defprotocol Visitor
  (visit [this]))

(extend-protocol Visitor

  Block
  (visit [this]
    {:block
     (into [] (for [child (get-children this)]
                (visit child)))})

  BlockQuote
  (visit [this]
    {:block-quote
     (into [] (for [child (get-children this)]
                (visit child)))})

  BulletList
  (visit [this]
    {:bullet-list
     (into [] (for [child (get-children this)]
                (visit child)))})

  Code
  (visit [this]
    {:code
     [{:text (.getLiteral this)}]})

  Document
  (visit [this]
    {:document
     (into [] (for [child (get-children this)]
                (visit child)))})

  Emphasis
  (visit [this]
    {:italic
     (into [] (for [child (get-children this)]
                (visit child)))})

  FencedCodeBlock
  (visit [this]
    {:fenced-code-block
     [{:char (.getFenceChar this)}
      {:indentation (.getFenceIndent this)}
      {:length (.getFenceLength this)}
      {:info (.getInfo this)}
      {:text (.getLiteral this)}]})

  HardLineBreak
  (visit [this]
    {:hard-line-break []})

  Heading
  (visit [this]
    {:heading
     (into [{:level (.getLevel this)}]
           (for [child (get-children this)]
             (visit child)))})

  HtmlBlock
  (visit [this]
    {:html-block
     [:text (.getLiteral this)]})

  HtmlInline
  (visit [this]
    {:html-inline
     [:text (.getLiteral this)]})

  Image
  (visit [this]
    {:image
     (into [{:title (.getTitle this)}
            {:destination (.getDestination this)}]
           (for [child (get-children this)]
             (visit child)))})

  IndentedCodeBlock
  (visit [this]
    {:indented-code-block
     [{:text (.getLiteral this)}]})

  Link
  (visit [this]
    {:link
     (into [{:title (.getTitle this)}
            {:destination (.getDestination this)}]
           (for [child (get-children this)]
             (visit child)))})

  ListItem
  (visit [this]
    {:list-item
     (into [] (for [child (get-children this)]
                (visit child)))})

  OrderedList
  (visit [this]
    {:ordered-list
     (into [] (for [child (get-children this)]
                (visit child)))})

  Paragraph
  (visit [this]
    {:paragraph
     (into [] (for [child (get-children this)]
                (visit child)))})

  StrongEmphasis
  (visit [this]
    {:bold
     (into [] (for [child (get-children this)]
                (visit child)))})

  SoftLineBreak
  (visit [this]
    {:soft-line-break []})

  TableBlock
  (visit [this]
    {:table-block
     (into [] (for [child (get-children this)]
                (visit child)))})

  TableHead
  (visit [this]
    {:table-head
     (into [] (for [child (get-children this)]
                (visit child)))})

  TableBody
  (visit [this]
    {:table-body
     (into [] (for [child (get-children this)]
                (visit child)))})

  TableRow
  (visit [this]
    {:table-row
     (into [] (for [child (get-children this)]
                (visit child)))})

  TableCell
  (visit [this]
    {:table-cell
     (into [] (for [child (get-children this)]
                (visit child)))})

  Text
  (visit [this]
    {:text (.getLiteral this)})

  ThematicBreak
  (visit [this]
    {:thematic-break []}))

(defn parse
  [md-string]
  (let [extention (list (TablesExtension/create))
        parser (.build (.extensions (Parser/builder) extention))
        md-document (.parse parser md-string)]
    (visit md-document)))
