import java.io.*;
import java.util.*;

public class Picture {

    public static void main(String[] args) throws FileNotFoundException {

	// Lines ==============================
	Canvas c = new Canvas(500, 500, 0, 0, 0);

	/*
	c = new Canvas();
	c.line(250, 250, 400, 250); // +x
	c.line(250, 250, 350, 300); // 1
	c.line(250, 250, 300, 350); // 2
	c.line(250, 250, 250, 400); // +y
	c.line(250, 250, 200, 350); // 3
	c.line(250, 250, 150, 300); // 4
	c.line(250, 250, 100, 250); // -x
	c.line(250, 250, 150, 200); // 5
	c.line(250, 250, 200, 150); // 6
	c.line(250, 250, 250, 100); // -y
	c.line(250, 250, 300, 150); // 7
	c.line(250, 250, 350, 200); // 8
	*/

	Pixel p = new Pixel(18, 10, 143);
	int x1, y1, x2, y2;
	for (int i = 0; i < 500; i += 5) {
	    x1 = i; y1 = 499;
	    x2 = 499; y2 = 499 - i;
	    c.line(x1, y1, x2, y2, p);
	}
	for (int i = 0; i < 500; i += 10) {
	    x1 = 499; y1 = 499 - i;
	    x2 = 499 - i; y2 = 0;
	    c.line(x1, y1, x2, y2, p);
	}
	for (int i = 0; i < 500; i += 15) {
	    x1 = 499 - i; y1 = 0;
	    x2 = 0; y2 = i;
	    c.line(x1, y1, x2, y2, p);
	}
	for (int i = 0; i < 500; i += 20) {
	    x1 = 0; y1 = i;
	    x2 = i; y2 = 499;
	    c.line(x1, y1, x2, y2, p);
	}
	c.save("out.ppm");
	// ==================================== */
	
	/* // Green Mountains =================
	Canvas c = new Canvas(1000, 500, 16, 0, 32);
	for (int y = 480; y > 0; y -= 10) {
	    for (int x = 0; x < 1000; x++) {
		if (Math.random() < 0.005) { // 0.5%
		    int g = (int)(256 * (1 - y / 500.));
		    int r = (int)(g * 0.35);
		    int b = (int)(g * 0.35);
		    c.triangle(x, y, new Pixel(r, g, b));
		}
	    }
	}
	c.save("out.ppm");
	// ==================================== */
    }
}

class Canvas {
    private Pixel[][] canvas; // Drawing Canvas
    private int x, y; // Dimensions

    // Constructors
    public Canvas() {
	canvas = new Pixel[500][500];
	x = 500;
	y = 500;
	fill(255, 255, 255);
    }
    public Canvas(int x, int y) {
	canvas = new Pixel[y][x];
	this.x = x;
	this.y = y;
	fill(255, 255, 255);
    }
    public Canvas(int x, int y, Pixel p) {
	this(x, y);
	fill(p);
    }
    public Canvas(int x, int y, int R, int G, int B) {
	this(x, y);
	fill(R, G, B);
    }

    // Accessors + Mutators
    public int[] getXY() {
	return new int[]{x, y};
    }
    public int getX() {
	return x;
    }
    public int getY() {
	return y;
    }

    // Canvas Methods
    public boolean draw_pixel(int x, int y, Pixel p) {
	canvas[y][x] = p;
	return true;
    }
    public boolean draw_pixel(int x, int y) {
	return draw_pixel(x, y, new Pixel(0, 0, 0));
    }
    public boolean draw_pixel(int x, int y, int R, int G, int B) {
	return draw_pixel(x, y, new Pixel(R, G, B));
    }

    public boolean fill(Pixel p) {
	for (int i = 0; i < y; i++) {
	    for (int j = 0; j < x; j++) {
		canvas[i][j] = p;
	    }
	}
	return true;
    }
    public boolean fill(int R, int G, int B) {
	return fill(new Pixel(R, G, B));
    }

    // Bresenham's Line Algorithm - 8 Octants
    public boolean line(int x1, int y1, int x2, int y2) {
	return line(x1, y1, x2, y2, new Pixel(0, 0, 0));
    }
    public boolean line(int x1, int y1, int x2, int y2, Pixel p) {
	if (x2 < x1) return line(x2, y2, x1, y1, p);
	int dy = y2 > y1 ? y2 - y1 : y1 - y2; // positive difference
	int dx = x2 - x1; // always positive
	int m = y2 > y1 ? 1 : -1;
	if (dy > dx)
	    if (m > 0)
		return line2(x1, y1, x2, y2, p); // Vertical - Octant 2
	    else
		return line7(x1, y1, x2, y2, p); // Vertical - Octant 7
	else
	    if (m > 0)
		return line1(x1, y1, x2, y2, p); // Horizontal - Octant 1
	    else
		return line8(x1, y1, x2, y2, p); // Horizontal - Octant 8
    }
    public boolean line7(int x1, int y1, int x2, int y2, Pixel p) {
	int A = y2 - y1; // dy
	int B = x1 - x2; // -dx
	int d = -2 * B + A;
	A = 2 * A;
	B = -2 * B;
	while (y1 >= y2) {
	    draw_pixel(x1, y1, p);
	    if (d > 0) {
		x1++;
		d += A;
	    }
	    y1--;
	    d += B;
	}
	return true;
    }
    public boolean line2(int x1, int y1, int x2, int y2, Pixel p) {
	int A = y2 - y1; // dy
	int B = x1 - x2; // -dx
	int d = 2 * B + A;
	A = 2 * A;
	B = 2 * B;
	while (y1 <= y2) {
	    draw_pixel(x1, y1, p);
	    if (d < 0) {
		x1++;
		d += A;
	    }
	    y1++;
	    d += B;
	}
	return true;
    }
    public boolean line8(int x1, int y1, int x2, int y2, Pixel p) {
	int A = y2 - y1; // dy
	int B = x1 - x2; // -dx
	int d = 2 * A - B;
	A = 2 * A;
	B = -2 * B;
	while (x1 <= x2) {
	    draw_pixel(x1, y1, p);
	    if (d < 0) {
		y1--;
		d += B;
	    }
	    x1++;
	    d += A;
	}
	return true;
    }
    public boolean line1(int x1, int y1, int x2, int y2, Pixel p) {
	int A = y2 - y1; // dy
	int B = x1 - x2; // -dx
	int d = 2 * A + B;
	A = 2 * A;
	B = 2 * B;
	while (x1 <= x2) {
	    draw_pixel(x1, y1, p);
	    if (d > 0) {
		y1++;
		d += B;
	    }
	    x1++;
	    d += A;
	}
	return true;
    }

    // Other Designs
    public boolean triangle(int x, int y, Pixel p) {
	int layer = 0;
	while (y > -1) {
	    for (int i = Math.max(0, x - layer); i < Math.min(x + layer + 1, this.x); i++) {
		canvas[y][i] = p;
	    }
	    layer++; y--;
	}
	return true;
    }

    // File Creation
    public boolean save(String name) throws FileNotFoundException {
	PrintWriter pw = new PrintWriter(new File(name));
	pw.print("P3 " + x + " " + y + " 255\n"); // Heading
	for (int i = y - 1; i > -1; i--) {
	    for (int j = 0; j < x; j++) {
		// System.out.printf("x: %d\ty: %d\n", j, i); // Debugging
		pw.print(canvas[i][j]);
	    }
	}
	pw.close();
	return true;
    }
}

class Pixel {
    private int R;
    private int G;
    private int B;

    // Constructors
    public Pixel() { // White Pixel
	R = 255;
	G = 255;
	B = 255;
    }
    public Pixel(int R, int G, int B) {
	this.R = R;
	this.G = G;
	this.B = B;
    }
    public Pixel(Pixel p) {
	int[] rgb = p.getRGB();
	R = rgb[0];
	G = rgb[1];
	B = rgb[2];
    }
    public Pixel(int flag) {
	if (flag == 1) { // Random
	    R = (int)(Math.random() * 256);
	    G = (int)(Math.random() * 256);
	    B = (int)(Math.random() * 256);
	}
    }
    
    // Accessors and Mutators
    public int[] getRGB() {
	return new int[]{R, G, B};
    }
    public String toString() {
	return "" + R + " " + G + " " + B + "\n";
    }
}
