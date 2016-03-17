# markdown2clj

A small Clojure library to parse markdown to clojure data structure.

This is a wrapper over [commonmark-java](https://github.com/atlassian/commonmark-java).

## Usage

```clojure
(let [document (parse "# Heading")]
 (println (:document document)))
```

## TODO
- Fix Readme
- Add Documentation
