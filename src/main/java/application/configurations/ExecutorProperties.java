package application.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("executor")
public class ExecutorProperties {

	private String keyspace;
	private int nThreads;
	private int nKeys;
	private int nOps;
	private int deltaRange;
	private int percentageDecs;
	private int sleepTimeMinMs;
	private int sleepTimeMaxMs;
	private int initValMin;
	private int initValMax;

	public int getDeltaRange() {
		return deltaRange;
	}

	public void setDeltaRange(int deltaRange) {
		this.deltaRange = deltaRange;
	}

	public int getnThreads() {
		return nThreads;
	}

	public void setnThreads(int nThreads) {
		this.nThreads = nThreads;
	}

	public String getKeyspace() {
		return keyspace;
	}

	public void setKeyspace(String keyspace) {
		this.keyspace = keyspace;
	}

	public int getnKeys() {
		return nKeys;
	}

	public void setnKeys(int nKeys) {
		this.nKeys = nKeys;
	}

	public int getPercentageDecs() {
		return percentageDecs;
	}

	public void setPercentageDecs(int percentageDecs) {
		this.percentageDecs = percentageDecs;
	}

	public int getnOps() {
		return nOps;
	}

	public void setnOps(int nOps) {
		this.nOps = nOps;
	}

	public int getSleepTimeMinMs() {
		return sleepTimeMinMs;
	}

	public void setSleepTimeMinMs(int sleepTimeMinMs) {
		this.sleepTimeMinMs = sleepTimeMinMs;
	}

	public int getSleepTimeMaxMs() {
		return sleepTimeMaxMs;
	}

	public void setSleepTimeMaxMs(int sleepTimeMaxMs) {
		this.sleepTimeMaxMs = sleepTimeMaxMs;
	}

	public int getInitValMin() {
		return initValMin;
	}

	public void setInitValMin(int initValMin) {
		this.initValMin = initValMin;
	}

	public int getInitValMax() {
		return initValMax;
	}

	public void setInitValMax(int initValMax) {
		this.initValMax = initValMax;
	}

}
