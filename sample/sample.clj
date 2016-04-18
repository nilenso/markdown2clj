{:document
 [{:heading [{:level 1} {:text "H1"}]}
  {:heading [{:level 2} {:text "H2"}]}
  {:heading [{:level 3} {:text "H3"}]}
  {:heading [{:level 4} {:text "H4"}]}
  {:heading [{:level 5} {:text "H5"}]}
  {:heading [{:level 6} {:text "H6"}]}
  {:paragraph
   [{:text "Emphasis, aka italics, with "}
    {:italic [{:text "asterisks"}]}
    {:text " or "}
    {:italic [{:text "underscores"}]}
    {:text "."}]}
  {:paragraph
   [{:text "Strong emphasis, aka bold, with "}
    {:bold [{:text "asterisks"}]}
    {:text " or "}
    {:bold [{:text "underscores"}]}
    {:text "."}]}
  {:paragraph
   [{:text "Combined emphasis with "}
    {:bold
     [{:text "asterisks and "} {:italic [{:text "underscores"}]}]}
    {:text "."}]}
  {:ordered-list
   [{:list-item [{:paragraph [{:text "First ordered list item"}]}]}
    {:list-item
     [{:paragraph [{:text "Another item"}]}
      {:bullet-list
       [{:list-item [{:paragraph [{:text "Unordered sub-list."}]}]}]}]}
    {:list-item
     [{:paragraph
       [{:text
         "Actual numbers don't matter, just that it's a number"}]}
      {:ordered-list
       [{:list-item [{:paragraph [{:text "Ordered sub-list"}]}]}]}]}
    {:list-item
     [{:paragraph [{:text "And another item."}]}
      {:paragraph
       [{:text
         "You can have properly indented paragraphs within list items. Notice the blank line above, and the leading spaces (at least one, but we'll use three here to also align the raw Markdown)."}]}
      {:paragraph
       [{:text
         "To have a line break without a paragraph, you will need to use two trailing spaces."}
        {:soft-line-break []}
        {:text
         "Note that this line is separate, but within the same paragraph."}
        {:soft-line-break []}
        {:text
         "(This is contrary to the typical GFM line break behaviour, where trailing spaces are not required.)"}]}]}]}
  {:bullet-list
   [{:list-item
     [{:paragraph [{:text "Unordered list can use asterisks"}]}]}]}
  {:bullet-list [{:list-item [{:paragraph [{:text "Or minuses"}]}]}]}
  {:bullet-list [{:list-item [{:paragraph [{:text "Or pluses"}]}]}]}
  {:paragraph
   [{:link
     [{:title nil}
      {:destination "https://www.google.com"}
      {:text "I'm an inline-style link"}]}]}
  {:paragraph
   [{:link
     [{:title "Google's Homepage"}
      {:destination "https://www.google.com"}
      {:text "I'm an inline-style link with title"}]}]}
  {:paragraph
   [{:link
     [{:title nil}
      {:destination "https://www.mozilla.org"}
      {:text "I'm a reference-style link"}]}]}
  {:paragraph
   [{:link
     [{:title nil}
      {:destination "../blob/master/LICENSE"}
      {:text "I'm a relative reference to a repository file"}]}]}
  {:paragraph
   [{:link
     [{:title nil}
      {:destination "http://slashdot.org"}
      {:text
       "You can use numbers for reference-style link definitions"}]}]}
  {:paragraph
   [{:text "Or leave it empty and use the "}
    {:link
     [{:title nil}
      {:destination "http://www.reddit.com"}
      {:text "link text itself"}]}
    {:text "."}]}
  {:paragraph
   [{:text
     "URLs and URLs in angle brackets will automatically get turned into links. "}
    {:link
     [{:title nil}
      {:destination "http://www.example.com"}
      {:text "http://www.example.com"}]}
    {:text
     " and sometimes example.com (but not on Github, for example)."}]}
  {:paragraph
   [{:text
     "Some text to show that the reference links can follow later."}]}
  {:paragraph
   [{:text "Here's our logo (hover to see the title text):"}]}
  {:paragraph
   [{:text "Inline-style:"}
    {:soft-line-break []}
    {:image
     [{:title "Logo Title Text 1"}
      {:destination
       "https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png"}
      {:text "alt text"}]}]}
  {:paragraph
   [{:text "Reference-style:"}
    {:soft-line-break []}
    {:image
     [{:title "Logo Title Text 2"}
      {:destination
       "https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png"}
      {:text "alt text"}]}]}
  {:paragraph
   [{:text "Inline "}
    {:code [{:text "code"}]}
    {:text " has "}
    {:code [{:text "back-ticks around"}]}
    {:text " it."}]}
  {:fenced-code-block
   [{:char \`}
    {:indentation 0}
    {:length 3}
    {:info "javascript"}
    {:text
     "var s = \"JavaScript syntax highlighting\";\nalert(s);\n"}]}
  {:fenced-code-block
   [{:char \`}
    {:indentation 0}
    {:length 3}
    {:info "python"}
    {:text "s = \"Python syntax highlighting\"\nprint s\n"}]}
  {:fenced-code-block
   [{:char \`}
    {:indentation 0}
    {:length 3}
    {:info ""}
    {:text
     "No language indicated, so no syntax highlighting.\nBut let's throw in a <b>tag</b>.\n"}]}
  {:paragraph [{:text "Colons can be used to align columns."}]}
  {:table-block
   [{:table-head
     [{:table-row
       [{:table-cell [{:text "Tables"}]}
        {:table-cell [{:text "Are"}]}
        {:table-cell [{:text "Cool"}]}]}]}
    {:table-body
     [{:table-row
       [{:table-cell [{:text "col 3 is"}]}
        {:table-cell [{:text "right-aligned"}]}
        {:table-cell [{:text "$1600"}]}]}
      {:table-row
       [{:table-cell [{:text "col 2 is"}]}
        {:table-cell [{:text "centered"}]}
        {:table-cell [{:text "$12"}]}]}
      {:table-row
       [{:table-cell [{:text "zebra stripes"}]}
        {:table-cell [{:text "are neat"}]}
        {:table-cell [{:text "$1"}]}]}]}]}
  {:block-quote
   [{:paragraph
     [{:text
       "Blockquotes are very handy in email to emulate reply text."}
      {:soft-line-break []}
      {:text "This line is part of the same quote."}]}]}
  {:paragraph [{:text "Quote break."}]}
  {:block-quote
   [{:paragraph
     [{:text
       "This is a very long line that will still be quoted properly when it wraps. Oh boy let's keep writing to make sure this is long enough to actually wrap for everyone. Oh, you can "}
      {:italic [{:text "put"}]}
      {:text " "}
      {:bold [{:text "Markdown"}]}
      {:text " into a blockquote."}]}]}]}
