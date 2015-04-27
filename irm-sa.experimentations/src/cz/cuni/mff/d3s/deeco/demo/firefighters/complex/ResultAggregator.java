package cz.cuni.mff.d3s.deeco.demo.firefighters.complex;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ResultAggregator {

	public static void main(String[] args) {
		File resultsDirectory = new File("results");
		List<Integer> values = new ArrayList<>();
		int count = resultsDirectory.listFiles().length;
		for (int i = 0; i < count; i++) {
			File f = resultsDirectory.listFiles()[i];
			try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			    String line;
			    int j = 0;
			    while ((line = br.readLine()) != null) {
			    	int value = Integer.parseInt(line);
			    	if (i == 0) {
			    		values.add(Integer.parseInt(line));
			    	} else {
			    		values.set(j, values.get(j) + value);
			    		j++;
			    	}
			    }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("results/aggregated.txt", true)))) {
			Double aggregated;
			for (Integer value: values) {
				aggregated = value * 1.0/count;
				out.println(aggregated);
				System.out.println(aggregated);
			}
		}catch (IOException e) {
		    e.printStackTrace();
		}
	}

}
