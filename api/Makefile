.EXPORT_ALL_VARIABLES:
SHELL = /bin/bash

# # #

GROUP_ID  = angara
ARTEFACT  = gpxtrack
VERSION   = $(shell cat VERSION)
DESCR     = GPX Track backend
REPO_URL  = https://github.com/angara/gpxtrack
MAIN      = gpt.main

DEV_ENV    ?= ../../conf/dev.env
DEV_CONFIG ?= ../../conf/dev.edn

# # #

JAVA_SRC  = ./src
TARGET 		= ./target
CLASSES 	= ${TARGET}/classes
UBER_RES  = ${TARGET}/resources
JAR_FILE  = ${TARGET}/${ARTEFACT}-${VERSION}.jar
UBER_JAR  = ${TARGET}/${ARTEFACT}.jar
BUILD_EDN = ${UBER_RES}/build.edn
APPNAME   = ${ARTEFACT}

TIMESTAMP ?= $(shell date -Isec)
COMMIT ?= $(shell git rev-parse HEAD)


.PHONY: dev nrepl clean javac inc-minor bump shapshot release uberjar

psql:
	@(set -a && source ${DEV_ENV} && psql "$${GPXTRACK__PSQL__URL}")

build_edn: VERSION
	mkdir -p ${UBER_RES}
	@echo "{">${BUILD_EDN}
	@echo ":appname \"${APPNAME}\"">>${BUILD_EDN}
	@echo ":version \"${VERSION}\"">>${BUILD_EDN}
	@echo ":commit \"${COMMIT}\"">>${BUILD_EDN}
	@echo ":timestamp \"${TIMESTAMP}\"">>${BUILD_EDN}
	@echo "}">>${BUILD_EDN}

pom: VERSION deps.edn
	@cat tools/pom-template.xml | envsubst > pom.xml
	@clojure -Spom

dev: export CONFIG_EDN = ${DEV_CONFIG}
dev:
	set -a && source ${DEV_ENV} && clojure -A:dev:nrepl

javac:
	javac ${JAVA_SRC}/mlib_org/mindrot/jbcrypt/BCrypt.java

uberjar: clean build_edn pom javac
	clojure -A:depstar:uberjar -m hf.depstar.uberjar ${UBER_JAR} --main ${MAIN} --compile

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
	rm -rf ${UBER_RES}
	rm -rf ${UBER_JAR}

#.
