package fr.dauphine.ja.student.pandemiage.gameengine;

import java.util.ArrayList;

import fr.dauphine.ja.pandemiage.common.Disease;

public class City {
	private String id;

	private String name;
	private String eigencentrality;
	private String degree;
	private String size;
	private String r;
	private String g;
	private String b;
	private String x;
	private String y;
	private int blue = 0;
	private int yellow = 0;
	private int black = 0;
	private int red = 0;
	private ArrayList<String> neighbours = new ArrayList<String>();

	public City(String id, String name, String eigencentrality, String degree, String size, String r, String g,
			String b, String x, String y) {
		this.id = id;
		this.name = name;
		this.eigencentrality = eigencentrality;
		this.degree = degree;
		this.size = size;
		this.r = r;
		this.g = g;
		this.b = b;
		this.x = x;
		this.y = y;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEigencentrality() {
		return eigencentrality;
	}

	public void setEigencentrality(String eigencentrality) {
		this.eigencentrality = eigencentrality;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getR() {
		return r;
	}

	public void setR(String r) {
		this.r = r;
	}

	public String getG() {
		return g;
	}

	public void setG(String g) {
		this.g = g;
	}

	public String getB() {
		return b;
	}

	public void setB(String b) {
		this.b = b;
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	public int getBlue() {
		return blue;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}

	public int getYellow() {
		return yellow;
	}

	public void setYellow(int yellow) {
		this.yellow = yellow;
	}

	public int getBlack() {
		return black;
	}

	public void setBlack(int black) {
		this.black = black;
	}

	public int getRed() {
		return red;
	}

	public void setRed(int red) {
		this.red = red;
	}

	public ArrayList<String> getNeighbours() {
		return neighbours;
	}

	public void setNeighbours(ArrayList<String> neighbours) {
		this.neighbours = neighbours;
	}

	@Override
	public String toString() {
		return "City [id=" + id + ", name=" + name + ", eigencentrality=" + eigencentrality + ", degree=" + degree
				+ ", size=" + size + ", r=" + r + ", g=" + g + ", b=" + b + ", x=" + x + ", y=" + y + ", blue=" + blue
				+ ", yellow=" + yellow + ", black=" + black + ", red=" + red + ", neighbours=" + neighbours + "]";
	}
}