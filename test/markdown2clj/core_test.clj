(ns markdown2clj.core-test
  (:require [clojure.test :refer :all]
            [markdown2clj.core :refer :all]))

(deftest parse-table
  (testing "parse md tables"
    (let [table-md "
| Tables        | Are     | Cool  |
| ------------- |---------| ------|
| col 3 is      | blah    | $1600 |
| col 2 is      | foobar  |   $12 |
| zebra stripes | are neat|    $1 |
"
          table-clj {:document
                     [{:table-block
                       [{:table-head
                         [{:table-row
                           [{:table-cell [{:text "Tables"}]}
                            {:table-cell [{:text "Are"}]}
                            {:table-cell [{:text "Cool"}]}]}]}
                        {:table-body
                         [{:table-row
                           [{:table-cell [{:text "col 3 is"}]}
                            {:table-cell [{:text "blah"}]}
                            {:table-cell [{:text "$1600"}]}]}
                          {:table-row
                           [{:table-cell [{:text "col 2 is"}]}
                            {:table-cell [{:text "foobar"}]}
                            {:table-cell [{:text "$12"}]}]}
                          {:table-row
                           [{:table-cell [{:text "zebra stripes"}]}
                            {:table-cell [{:text "are neat"}]}
                            {:table-cell [{:text "$1"}]}]}]}]}]}]
      (is (= table-clj (parse table-md))))))

(deftest parse-heading
  (let [get-lvl #(-> % :document first :heading first :level)]

    (testing "parse returns a representative map"
      (is (= {:document [{:heading [{:level 1}
                                    {:text "Heading"}]}]}
             (parse "# Heading"))))

    (testing "parse correctly identifies a level 1 heading"
      (is (= 1 (get-lvl (parse "# Heading")))))

    (testing "parse correctly identifies a level 2 heading"
      (is (= 2 (get-lvl (parse "## H2")))))

    (testing "parse correctly identifies a level 3 heading"
      (is (= 3 (get-lvl (parse "### H3")))))

    (testing "parse correctly identifies a level 4 heading"
      (is (= 4 (get-lvl (parse "#### H4")))))

    (testing "parse correctly identifies a level 5 heading"
      (is (= 5 (get-lvl (parse "##### H5")))))

    (testing "parse correctly identifies a level 6 heading"
      (is (= 6 (get-lvl (parse "###### H6")))))))

(deftest parse-emphasis
  (testing "parse bold"
    (let [obi-wan "**In my experience there is no such thing as luck.**"
          obi-wan-clj {:document
                       [{:paragraph
                         [{:bold
                           [{:text "In my experience there is no such thing as luck."}]}]}]}]
      (is (= obi-wan-clj (parse obi-wan)))))

  (testing "parse italics"
    (let [yoda "*When nine hundred years old you reach, look as good you will not.*"
          yoda-clj {:document
                       [{:paragraph
                         [{:italic
                           [{:text "When nine hundred years old you reach, look as good you will not."}]}]}]}]
      (is (= yoda-clj (parse yoda)))))

  (testing "parse both bold and italics together"
    (let [yoda "**Do**. Or **do not**. There is no *try*"
          yoda-clj {:document
                    [{:paragraph
                      [{:bold [{:text "Do"}]}
                       {:text ". Or "}
                       {:bold [{:text "do not"}]}
                       {:text ". There is no "}
                       {:italic [{:text "try"}]}]}]}]
      (is (= yoda-clj (parse yoda))))))

(deftest parse-ordered-lists
  (testing "test ordered list"
    (let [quotes "1. May the Force be with you
  2. I find your lack of faith disturbing.
  3. I've got a very bad feeling about this.
  4. Never tell me the odds!
  5. Truly wonderful, the mind of a child is."
          quotes-clj {:document
                      [{:ordered-list
                        [{:list-item [{:paragraph [{:text "May the Force be with you"}]}]}
                         {:list-item [{:paragraph [{:text "I find your lack of faith disturbing."}]}]}
                         {:list-item [{:paragraph [{:text "I've got a very bad feeling about this."}]}]}
                         {:list-item [{:paragraph [{:text "Never tell me the odds!"}]}]}
                         {:list-item [{:paragraph [{:text "Truly wonderful, the mind of a child is."}]}]}]}]}]
      (is (= quotes-clj (parse quotes)))))

  (testing "test nested ordered list"
    (let [nested-list "1. First
     2. Second
        3. Third"
          nested-list-clj {:document
                           [{:ordered-list
                             [{:list-item [{:paragraph [{:text "First"}]}
                                {:ordered-list
                                 [{:list-item [{:paragraph [{:text "Second"}]}
                                    {:ordered-list
                                     [{:list-item [{:paragraph [{:text "Third"}]}]}]}]}]}]}]}]}]
      (is (= nested-list-clj (parse nested-list))))))

(deftest parse-link
  (testing "parse on simple link representation"
    (is (= {:document
            [{:paragraph
              [{:link
                [{:title nil}
                 {:destination "https://www.google.com"}
                 {:text "I'm an inline-style link"}]}]}]}
           (parse "[I'm an inline-style link](https://www.google.com)"))))

  (testing "parse on link with title"
    (is (= {:document [{:paragraph
                        [{:link
                          [{:title "Google's Homepage"}
                           {:destination "https://www.google.com"}
                           {:text "I'm an inline-style link with title"}]}]}]}
           (parse "[I'm an inline-style link with title](https://www.google.com \"Google's Homepage\")"))))

  (testing "parse on reference style links"
    (is (= {:document [{:paragraph
                        [{:link
                          [{:title nil}
                           {:destination "https://www.mozilla.org"}
                           {:text "I'm a reference-style link"}]}]}]}
         (parse "[I'm a reference-style link][Arbitrary case-insensitive reference text]

[arbitrary case-insensitive reference text]: https://www.mozilla.org"))))

  (testing "parse on links enclosed in angled brackets"
    (is (= {:document [{:paragraph
                        [{:link
                          [{:title nil}
                           {:destination "http://www.example.com"}
                           {:text "http://www.example.com"}]}]}]}
         (parse "<http://www.example.com>")))))

(deftest parse-image
  (testing "parse on simple image representaion"
    (is (= {:document [{:paragraph
                        [{:image
                          [{:title "Logo Title Text 1"}
                           {:destination "/img/path/img.img"}
                           {:text "alt text"}]}]}]}
           (parse "![alt text](/img/path/img.img \"Logo Title Text 1\")"))))

  (testing "parse on reference image representation"
    (is (={:document [{:paragraph
                        [{:image
                          [{:title "Logo Title Text 2"}
                           {:destination "/img/path/img.img"}
                           {:text "alt text"}]}]}]}
         (parse "![alt text][logo]

[logo]: /img/path/img.img \"Logo Title Text 2\"")))))
