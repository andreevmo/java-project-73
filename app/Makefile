setup:
	gradle wrapper --gradle-version 7.4

clean:
	./gradlew clean

build:
	./gradlew clean build

start:
	./gradlew run

install:
	./gradlew install

start-dist:
	java -jar -Dspring.profiles.active=production app/build/libs/app-1.0-SNAPSHOT.jar

generate-migrations:
	./gradlew generateMigrations

lint:
	./gradlew checkstyleMain checkstyleTest

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

check-updates:
	./gradlew dependencyUpdates

.PHONY: build