{:dev {:plugins [[lein-set-version "0.3.0"]]
       :aliases {"release" ["pallet" "-P" "localhost" "up"
                            "--quiet" "--phases" "release"]}}

 :pallet {:dependencies [[com.palletops/pallet "0.8.0-SNAPSHOT"]
                         [com.palletops/java-crate "0.8.0-SNAPSHOT"]
                         [com.palletops/git-crate "0.8.0-SNAPSHOT"]
                         [com.palletops/lein-crate "0.8.0-alpha.1"]]}

 :release
 {:plugins [[lein-set-version "0.2.2"]]
  :set-version {:updates [{:path "README.md" :no-snapshot true}]}}}
