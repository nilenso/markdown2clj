# markdown2clj

[![Clojars Project](https://img.shields.io/clojars/v/markdown2clj.svg)](https://clojars.org/markdown2clj)


A small Clojure library to parse markdown to clojure data structure.

This is a wrapper over [commonmark-java](https://github.com/atlassian/commonmark-java).

## Usage

Add the following to your `:dependencies` in `project.clj`.
```clojure
[markdown2clj "0.1.3-SNAPSHOT"]
```
In your code
```clojure
(:require markdown2clj.core :as md)

(md/parse "# Heading")
=> {:document [{:heading [{:level 1}
			  {:text "Heading"}]}]}
```
where `markdown-string` is the string you want to parse and `params` is the optional parameters.

Valid params are :
- `:parse-tabbed-tables` which parses tables that are indented.


This library supports the commonmark specs as supported by the commonmarkjava library. Which are
- Paragraph
- Bold & Italics
- Heading
- Table
- Link
- Images
- Inline HTML
- Indented Code Block
- Ordered Lists
- Bullet Lists
- Blockquote

TODO:
- Add `sample.md` & `sample.clj`