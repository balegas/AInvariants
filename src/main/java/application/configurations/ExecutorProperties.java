package application.configurations;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.datastax.driver.core.ConsistencyLevel;

@ConfigurationProperties("executor")
public class ExecutorProperties {

    private String keyspace;
    private int nThreads;
    private int nKeys;
    private int executionTime;
    private int deltaRange;
    private int percentageRO;
    private int percentageDecs;
    private int initValMin;
    private int initValMax;
    private int minSleepTime;
    private int maxSleepTime;
    private int clientAdjustInterval;
    private String sleepGeneratorClass;
    private String sleepDistArg0;
    private String sleepDistArg1;
    private String sleepDistArg2;
    private String keyGeneratorClass;
    private String keyDistArg0;
    private String keyDistArg1;
    private String keyDistArg2;
    private String valueGeneratorClass;
    private String valueDistArg0;
    private String valueDistArg1;
    private String valueDistArg2;
    private String clientsGeneratorClass;
    private String clientDistArg0;
    private String clientDistArg1;
    private String clientDistArg2;
    private String outputFile;
    private int printIntervalMS;
    private ConsistencyLevel consistency;
    private List<String> dcNames;
    private List<String> endpoints;

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

    public int getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(int executionTime) {
        this.executionTime = executionTime;
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

    public String getSleepGeneratorClass() {
        return sleepGeneratorClass;
    }

    public void setSleepGeneratorClass(String sleepGeneratorClass) {
        this.sleepGeneratorClass = sleepGeneratorClass;
    }

    public String getSleepDistArg0() {
        return sleepDistArg0;
    }

    public void setSleepDistArg0(String sleepDistArg0) {
        this.sleepDistArg0 = sleepDistArg0;
    }

    public String getSleepDistArg1() {
        return sleepDistArg1;
    }

    public void setSleepDistArg1(String sleepDistArg1) {
        this.sleepDistArg1 = sleepDistArg1;
    }

    public String getSleepDistArg2() {
        return sleepDistArg2;
    }

    public void setSleepDistArg2(String sleepDistArg2) {
        this.sleepDistArg2 = sleepDistArg2;
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

    public ConsistencyLevel getConsistency() {
        return consistency;
    }

    public void setConsistency(ConsistencyLevel consistency) {
        this.consistency = consistency;
    }

    public List<String> getDcNames() {
        return dcNames;
    }

    public void setDcNames(List<String> dcNames) {
        this.dcNames = dcNames;
    }

    public List<String> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<String> endpoints) {
        this.endpoints = endpoints;
    }

    public int getClientAdjustInterval() {
        return clientAdjustInterval;
    }

    public void setClientAdjustInterval(int clientAdjustInterval) {
        this.clientAdjustInterval = clientAdjustInterval;
    }

    public String getClientsGeneratorClass() {
        return clientsGeneratorClass;
    }

    public void setClientsGeneratorClass(String clientsGeneratorClass) {
        this.clientsGeneratorClass = clientsGeneratorClass;
    }

    public String getClientDistArg0() {
        return clientDistArg0;
    }

    public void setClientDistArg0(String clientDistArg0) {
        this.clientDistArg0 = clientDistArg0;
    }

    public String getClientDistArg1() {
        return clientDistArg1;
    }

    public void setClientDistArg1(String clientDistArg1) {
        this.clientDistArg1 = clientDistArg1;
    }

    public String getClientDistArg2() {
        return clientDistArg2;
    }

    public void setClientDistArg2(String clientDistArg2) {
        this.clientDistArg2 = clientDistArg2;
    }

    public int getMinSleepTime() {
        return minSleepTime;
    }

    public void setMinSleepTime(int minSleepTime) {
        this.minSleepTime = minSleepTime;
    }

    public int getMaxSleepTime() {
        return maxSleepTime;
    }

    public void setMaxSleepTime(int maxSleepTime) {
        this.maxSleepTime = maxSleepTime;
    }

    @Override
    public String toString() {
        return "ExecutorProperties [keyspace=" + keyspace + ", nThreads=" + nThreads + ", nKeys=" + nKeys
                + ", executionTime=" + executionTime + ", deltaRange=" + deltaRange + ", percentageRO=" + percentageRO
                + ", percentageDecs=" + percentageDecs + ", initValMin=" + initValMin + ", initValMax=" + initValMax
                + ", minSleepTime=" + minSleepTime + ", maxSleepTime=" + maxSleepTime + ", clientAdjustInterval="
                + clientAdjustInterval + ", sleepGeneratorClass=" + sleepGeneratorClass + ", sleepDistArg0="
                + sleepDistArg0 + ", sleepDistArg1=" + sleepDistArg1 + ", sleepDistArg2=" + sleepDistArg2
                + ", keyGeneratorClass=" + keyGeneratorClass + ", keyDistArg0=" + keyDistArg0 + ", keyDistArg1="
                + keyDistArg1 + ", keyDistArg2=" + keyDistArg2 + ", valueGeneratorClass=" + valueGeneratorClass
                + ", valueDistArg0=" + valueDistArg0 + ", valueDistArg1=" + valueDistArg1 + ", valueDistArg2="
                + valueDistArg2 + ", clientsGeneratorClass=" + clientsGeneratorClass + ", clientDistArg0="
                + clientDistArg0 + ", clientDistArg1=" + clientDistArg1 + ", clientDistArg2=" + clientDistArg2
                + ", outputFile=" + outputFile + ", printIntervalMS=" + printIntervalMS + ", consistency=" + consistency
                + ", dcNames=" + dcNames + ", endpoints=" + endpoints + "]";
    }

}
