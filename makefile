all: Picture.java
	javac Picture.java

run: Picture.class
	java Picture

clean: 
	rm *.class *.ppm *~

jpg: out.ppm
	java Picture; \
	convert out.ppm out.jpg

png: out.ppm
	java Picture; \
	convert out.ppm out.png

