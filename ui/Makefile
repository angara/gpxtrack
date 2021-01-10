.EXPORT_ALL_VARIABLES:
SHELL = /bin/bash

# # # # #

DEV_RES   = ../srv/target/devres
PROD_RES  = ../srv/target/resources

# # # # #

.PHONY: cssdev prod_assets tailwindcss build_js prodpack

cssdev:
	mkdir -p ${DEV_RES}/public/
	cp -r assets ${DEV_RES}/public/
	npx tailwindcss build assets/css/styles.css -o ${DEV_RES}/public/assets/css/styles.css

prod_assets:
	@echo "prod assets"
	@mkdir -p ${PROD_RES}/public
	@cp -r assets ${PROD_RES}/public

tailwindcss: export NODE_ENV = production
tailwindcss:
	@echo "build css"
	@npx tailwindcss build assets/css/styles.css -o ${PROD_RES}/public/assets/css/styles.css

build_js:
	@npx shadow-cljs release prod

prodpack: prod_assets build_js tailwindcss
	@echo "prod: assets, js, css."

#.