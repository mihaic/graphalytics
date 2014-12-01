package org.tudelft.graphalytics.mapreducev2;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.tudelft.graphalytics.Graph;

public abstract class MapReduceJobLauncher extends Configured implements Tool {
	
	private boolean graphIsDirected;
	private boolean graphIsEdgeBased;
	private String inputPath;
	private String intermediatePath;
	private String outputPath;
	protected Object parameters;
	protected int numMappers;
	protected int numReducers;
	
	public MapReduceJobLauncher() {
		graphIsDirected = graphIsEdgeBased = false;
		parameters = null;
		inputPath = intermediatePath = outputPath = "";
		numMappers = numReducers = -1;
	}

	public void parseGraphData(Graph graph, Object parameters) {
		graphIsDirected = graph.isDirected();
		graphIsEdgeBased = graph.isEdgeBased();
		
		this.parameters = parameters;
	}
    
    public void setInputPath(String path) {
    	inputPath = path;
    }
    
    public void setIntermediatePath(String path) {
    	intermediatePath = path;
    }
    
    public void setOutputPath(String path) {
    	outputPath = path;
    }
    
	public void setNumMappers(int numMappers) {
		this.numMappers = numMappers;
	}
	
	public void setNumReducers(int numReducers) {
		this.numReducers = numReducers;
	}
	
	@Override
    public int run(String[] args) throws Exception {
        // Create the appropriate job
		ToolRunnerJob<?> job;
        if (graphIsDirected)
        	job = createDirectedJob(inputPath, intermediatePath, outputPath);
        else
        	job = createUndirectedJob(inputPath, intermediatePath, outputPath);
        
        // Update configuration
        job.setNumMappers(numMappers);
        job.setNumReducers(numReducers);
        
        // Run it!
    	return ToolRunner.run(getConf(), job, args);
    }
	
	protected abstract ToolRunnerJob<?> createDirectedJob(String input, String intermediate, String output);
	protected abstract ToolRunnerJob<?> createUndirectedJob(String input, String intermediate, String output);
	
}