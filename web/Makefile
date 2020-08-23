
.EXPORT_ALL_VARIABLES:
.PHONY: cssdev prod_assets tailwindcss build_js prodpack

DEV_RES   = ./build/dev
PROD_RES  = ./build/prod

# cssdev:
# 	mkdir -p ${DEV_RES}/public/
# 	cp -r assets/* ${DEV_RES}/public/
# 	npx tailwindcss build assets/css/styles.css -o ${DEV_RES}/public/css/styles.css

prod_assets:
	@echo "prod assets"
	@mkdir -p ${PROD_RES}/public
	@cp -r assets/* ${PROD_RES}/public

tailwindcss: export NODE_ENV = production
tailwindcss:
	@echo "build css"
	@npx tailwindcss build assets/css/styles.css -o ${PROD_RES}/public/css/styles.css

build_js:
	@npx shadow-cljs release prod

prodpack: prod_assets build_js tailwindcss
	@echo "prod: assets, js, css."
