;;; Pallet project configuration file

;;; By default, the pallet.api and pallet.crate namespaces are already referred.
;;; The pallet.crate.automated-admin-user/automated-admin-user us also referred.

(require '[pallet.crate.git :refer [git git-cmdline]]
         '[pallet.crate.lein :refer [lein leiningen]]
         '[pallet.crate.java :refer [java]])

;;; Files that contain version information
(def release-files ["project.clj" "ReleaseNotes.md" "README.md"])

(defplan editor [path]
  (exec-checked-script
   (str "Edit " path)
   (@EDITOR path)))

(defplan release
  [previous-version version next-version]
  ;; pre-check tests pass
  (lein "do" "clean," "test")

  (git "flow" "release" "start" version) ; start release branch
  (lein "with-profile" "+release" "set-version" version
        ":previous-version" previous-version) ; update version strings

  ;; edit meta files
  (git "--no-pager" "log" "--pretty=changelog" (str previous-version ".."))

  (doseq [f release-files]
    (editor f))
  (apply git "add" release-files)
  (git "commit" "-m"
       (str "Updated project.clj, release notes and readme for " version))

  (lein "do" "clean," "test," "deploy" "clojars")
  (git "flow" "release" "finish" version)
  (lein "with-profile" "+release" "set-version" next-version)
  (apply git "add" release-files)
  (git "commit" "-m" "Updated version for next release cycle"))

(defproject pallet-codox
  :groups
  [(group-spec
    "release"
    :extends [(java {}) (git-cmdline {}) (leiningen {:dir ""})]
    :phases
    {:release (fn [previous-version version next-version]
                (with-action-options {:script-prefix :no-sudo}
                  (release previous-version version next-version)))})])
