package application.configurations;

import java.util.Arrays;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.datastax.driver.core.ConsistencyLevel;

@ConfigurationProperties("executor")
public class ExecutorProperties {

    private String keyspace;
    private int nThreads;
    private int nKeys;
    private int nOps;
    private int deltaRange;
    private int percentageRO;
    private int percentageDecs;
    private int sleepTimeMinMs;
    private int sleepTimeMaxMs;
    private int initValMin;
    private int initValMax;
    private String keyGeneratorClass;
    private String keyDistArg0;
    private String keyDistArg1;
    private String keyDistArg2;
    private String valueGeneratorClass;
    private String valueDistArg0;
    private String valueDistArg1;
    private String valueDistArg2;
    private String outputFile;
    private int printIntervalMS;
    private String[] cassandraEndpoints;
    private ConsistencyLevel consistency;
    private String dcName;

    public String getKeyspace() {
        return keyspace;
    }

    public void setKeyspace(String keyspace) {
        this.keyspace = keyspace;
    }

    public int getnThreads() {
        return nThreads;
    }

    public void setnThreads(int nThreads) {
        this.nThreads = nThreads;
    }

    public int getnKeys() {
        return nKeys;
    }

    public void setnKeys(int nKeys) {
        this.nKeys = nKeys;
    }

    public int getnOps() {
        return nOps;
    }

    public void setnOps(int nOps) {
        this.nOps = nOps;
    }

    public int getDeltaRange() {
        return deltaRange;
    }

    public void setDeltaRange(int deltaRange) {
        this.deltaRange = deltaRange;
    }

    public int getPercentageRO() {
        return percentageRO;
    }

    public void setPercentageRO(int percentageRO) {
        this.percentageRO = percentageRO;
    }

    public int getPercentageDecs() {
        return percentageDecs;
    }

    public void setPercentageDecs(int percentageDecs) {
        this.percentageDecs = percentageDecs;
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

    public String getKeyGeneratorClass() {
        return keyGeneratorClass;
    }

    public void setKeyGeneratorClass(String keyGeneratorClass) {
        this.keyGeneratorClass = keyGeneratorClass;
    }

    public String getKeyDistArg0() {
        return keyDistArg0;
    }

    public void setKeyDistArg0(String keyDistArg0) {
        this.keyDistArg0 = keyDistArg0;
    }

    public String getKeyDistArg1() {
        return keyDistArg1;
    }

    public void setKeyDistArg1(String keyDistArg1) {
        this.keyDistArg1 = keyDistArg1;
    }

    public String getKeyDistArg2() {
        return keyDistArg2;
    }

    public void setKeyDistArg2(String keyDistArg2) {
        this.keyDistArg2 = keyDistArg2;
    }

    public String getValueGeneratorClass() {
        return valueGeneratorClass;
    }

    public void setValueGeneratorClass(String valueGeneratorClass) {
        this.valueGeneratorClass = valueGeneratorClass;
    }

    public String getValueDistArg0() {
        return valueDistArg0;
    }

    public void setValueDistArg0(String valueDistArg0) {
        this.valueDistArg0 = valueDistArg0;
    }

    public String getValueDistArg1() {
        return valueDistArg1;
    }

    public void setValueDistArg1(String valueDistArg1) {
        this.valueDistArg1 = valueDistArg1;
    }

    public String getValueDistArg2() {
        return valueDistArg2;
    }

    public void setValueDistArg2(String valueDistArg2) {
        this.valueDistArg2 = valueDistArg2;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public int getPrintIntervalMS() {
        return printIntervalMS;
    }

    public void setPrintIntervalMS(int printIntervalMS) {
        this.printIntervalMS = printIntervalMS;
    }

    public String[] getCassandraEndpoints() {
        return cassandraEndpoints;
    }

    public void setCassandraEndpoints(String[] cassandraEndpoints) {
        this.cassandraEndpoints = cassandraEndpoints;
    }

    public ConsistencyLevel getConsistency() {
        return consistency;
    }

    public void setConsistency(ConsistencyLevel consistency) {
        this.consistency = consistency;
    }

    public String getDcName() {
        return dcName;
    }

    public void setDcName(String dcName) {
        this.dcName = dcName;
    }

    @Override
    public String toString() {
        return "ExecutorProperties [keyspace=" + keyspace + ", nThreads=" + nThreads + ", nKeys=" + nKeys + ", nOps="
                + nOps + ", deltaRange=" + deltaRange + ", percentageRO=" + percentageRO + ", percentageDecs="
                + percentageDecs + ", sleepTimeMinMs=" + sleepTimeMinMs + ", sleepTimeMaxMs=" + sleepTimeMaxMs
                + ", initValMin=" + initValMin + ", initValMax=" + initValMax + ", keyGeneratorClass="
                + keyGeneratorClass + ", keyDistArg0=" + keyDistArg0 + ", keyDistArg1=" + keyDistArg1 + ", keyDistArg2="
                + keyDistArg2 + ", valueGeneratorClass=" + valueGeneratorClass + ", valueDistArg0=" + valueDistArg0
                + ", valueDistArg1=" + valueDistArg1 + ", valueDistArg2=" + valueDistArg2 + ", outputFile=" + outputFile
                + ", printIntervalMS=" + printIntervalMS + ", cassandraEndpoints=" + Arrays.toString(cassandraEndpoints)
                + ", consistency=" + consistency + ", dcName=" + dcName + "]";
    }

}
