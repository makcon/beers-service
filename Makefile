.PHONY: down up test

down:
	docker compose down --remove-orphans

up:
	docker compose run -d -p "8080:8080" beer-tap-api mvn -pl app -am clean spring-boot:run -Dtests.skip=true

test:
	docker compose run --rm --no-deps -p "8080:8080" beer-tap-api mvn verify

coverage: test