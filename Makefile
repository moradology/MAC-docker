build:
	./sbt assembly
	docker build .
