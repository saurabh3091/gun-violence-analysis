# US Gun Violence Analyses

### Prerequisites

Gradle, 
Scala 2.11,
Spark 2.2.1

### Build
goto the gun-violence-analysis and run
```
gradle fatJar
```

## Run on Local

```
spark-submit --master "local[*]" --class com.data.GunViolenceAnalyzer build/libs/gun-violence-analysis-0.1.jar path-to-csv
```


## Run on YARN

```
export HADOOP_CONF_DIR=XXX
spark-submit \
  --class com.data.GunViolenceAnalyzer \
  --master yarn \
  --deploy-mode cluster \  # can be client for client mode
  --executor-memory 128M \
  --num-executors 3 \
  build/libs/gun-violence-analysis-0.1.jar path-to-csv
```