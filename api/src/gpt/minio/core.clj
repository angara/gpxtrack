(ns gpt.minio.core
  (:import
    [java.io InputStream]
    [io.minio MinioClient GetObjectArgs PutObjectArgs])
    ;[io.minio.errors MinioException])
  (:require
    [gpt.cfg  :as cfg]))
;=

(set! *warn-on-reflection* true)

(def ^:cost PART_SIZE (* 10 1024 1024))

(defn ^MinioClient make-client [{:keys [url access-key secret-key]}]
  (.build
    (doto (MinioClient/builder)
      (.endpoint url)
      (.credentials access-key secret-key))))
;;


(defn ^InputStream get-object [^MinioClient client ^String bucket ^String name]
  (let [args
        (doto (GetObjectArgs/builder)
          (.bucket bucket)
          (.object name))]
    (.getObject client (.build args))))
;;

(defn put-object [^MinioClient client ^String bucket ^String name ^InputStream data]
  (let [args
        (doto (PutObjectArgs/builder)
          (.bucket bucket)
          (.object name)
          (.stream data -1 PART_SIZE))]
          ;; (.contentType "...")
    (.putObject client (.build args))))
;;

(comment

  (def minio
    (make-client cfg/minio))

  (doseq [b (.listBuckets minio)]
    (prn (.name b)))


  (get-object minio (:bucket cfg/app) "test.gpx")


  (let [data "qwertyasdf 123423452345"
        bais (java.io.ByteArrayInputStream. (.getBytes data "UTF-8"))]
    (put-object minio (:bucket cfg/app) "test2" bais))
    
  ,)
