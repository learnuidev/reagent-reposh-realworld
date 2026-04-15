(ns app.util)

(defmacro lazy-component [the-sym]
  `(app.util/lazy-component* (shadow.lazy/loadable ~the-sym)))
