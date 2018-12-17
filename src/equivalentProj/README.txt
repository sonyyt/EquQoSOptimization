1. read certain file for all equations;  DONE
2. read/write QoS of MS;                DONE
3. input constraints, output results.   DONE



a: reliability = 0.367; cost = 17.0; latency = 15.0
b: reliability = 0.897; cost = 7.0; latency = 15.0
c: reliability = 0.139; cost = 7.0; latency = 19.0
d: reliability = 0.847; cost = 10.0; latency = 8.0

(a-(b-c)*d): reliability = 0.991411140133; cost = 28.217393; latency = 20.931476492999998


MISTAKES:
1) a*b*c != (a*b)*c; 
2) include equations with length less than n. 