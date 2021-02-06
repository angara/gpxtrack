.EXPORT_ALL_VARIABLES:
SHELL = /bin/bash

# # #

GROUP_ID  = angara
ARTIFACT  = gpxtrack
VERSION   = $(shell cat VERSION)
DESCR     = GPX Track backend
REPO_URL  = https://github.com/angara/gpxtrack
MAIN      = gpt.main

DEV_ENV    ?= ../../conf/dev.env
DEV_CONFIG ?= ../../conf/dev.edn

# # #

JAVA_SRC  = ./src
TARGET 		= ./target
RESOURCES = ./resources
CLASSES 	= ${TARGET}/classes
DEV_RES   = ${TARGET}/devres
UBER_RES  = ${TARGET}/resources
JAR_FILE  = ${TARGET}/${ARTIFACT}-${VERSION}.jar
UBER_JAR  = ${TARGET}/${ARTIFACT}.jar
BUILD_EDN = ${UBER_RES}/build.edn
APPNAME   = ${ARTIFACT}

TIMESTAMP ?= $(shell date -Isec)
COMMIT    ?= $(shell git rev-parse HEAD)

# # #

.PHONY: dev build psql clean javac inc-minor bump shapshot release uberjar outdated

dev:
	set -a && CONFIG_EDN=${DEV_CONFIG} && source ${DEV_ENV} && clojure -M:dev:nrepl

psql:
	@(set -a && source ${DEV_ENV} && psql "$${GPXTRACK__PSQL__URL}")

build_edn: VERSION
	@echo "build.edn"
	@mkdir -p ${UBER_RES}
	@echo "{">${BUILD_EDN}
	@echo ":appname \"${APPNAME}\"">>${BUILD_EDN}
	@echo ":version \"${VERSION}\"">>${BUILD_EDN}
	@echo ":commit \"${COMMIT}\"">>${BUILD_EDN}
	@echo ":timestamp \"${TIMESTAMP}\"">>${BUILD_EDN}
	@echo "}">>${BUILD_EDN}

javac:
	javac ${JAVA_SRC}/mlib_org/mindrot/jbcrypt/BCrypt.java

uberjar: build_edn javac
	clojure -X:depstar uberjar :aliases '[:uber-resources]' :sync-pom true :aot true :jar ${UBER_JAR} \
		:main-class ${MAIN} :group-id ${GROUP_ID} :artifact-id ${ARTIFACT} :version '"${VERSION}"'
#

prod_res:
	(cd ../ui && ls)
	pwd

build: clean uberjar prod_res

# snapshot: export VERSION := ${VERSION}-SNAPSHOT
# snapshot: uberjar
# 	@mvn deploy:deploy-file 		\
# 		-DpomFile=pom.xml					\
# 		-Dfile=${UBER_JAR} 				\
# 		-DrepositoryId=${REPO_ID}	\
# 		-Durl=${SNAPSHOTS_URL}

# release: uberjar
# 	@mvn deploy:deploy-file 		\
# 		-DpomFile=pom.xml					\
# 		-Dfile=${UBER_JAR} 				\
# 		-DrepositoryId=${REPO_ID}	\
# 		-Durl=${RELEASES_URL}

inc-minor:
	@(VERS=`awk -F'.' '{print $$1 "." $$2+1 "." $$3+1}' VERSION` && echo $${VERS} > VERSION)
	@cat VERSION

bump:
	@(VERS=`awk -F'.' '{print $$1 "." $$2 "." $$3+1}' VERSION` && echo $${VERS} > VERSION)
	@cat VERSION

clean:
	rm -rf ${DEV_RES}
	rm -rf ${UBER_RES}
	rm -rf ${UBER_JAR}

outdated:
	@(clojure -Sdeps '{:deps {antq/antq {:mvn/version "RELEASE"}}}' -X antq.core/-main || exit 0)

#.
