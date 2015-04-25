package cz.cuni.mff.d3s.deeco.demo.firefighters.complex;

import java.io.File;

public class Cleaner {

	public static void main(String[] args) {
		File directory = new File("results");
		for(File f: directory.listFiles()) {
			 f.delete();
		}
	}

}
