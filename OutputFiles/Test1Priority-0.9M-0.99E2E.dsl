WARP program for graph SeeSpray
Scheduler Name: Priority
M: 0.9
E2E: 0.99
nChannels: 16
Time Slot	B1	C1
0	wait(#1)	if has(F1A) push(F1A: C1 -> B1, #1)
1	wait(#2)	if has(F1A) push(F1A: C1 -> B1, #2)
2	sleep	sleep
3	sleep	sleep
4	sleep	sleep
5	sleep	sleep
6	sleep	sleep
7	sleep	sleep
8	sleep	sleep
9	sleep	sleep
10	sleep	sleep
11	sleep	sleep
12	sleep	sleep
13	sleep	sleep
14	sleep	sleep
15	sleep	sleep
16	sleep	sleep
17	sleep	sleep
18	sleep	sleep
19	sleep	sleep
20	sleep	sleep
21	sleep	sleep
22	sleep	sleep
23	sleep	sleep
24	sleep	sleep
25	if has(F1B) push(F1B: B1 -> C1, #1)	wait(#1)
26	if has(F1B) push(F1B: B1 -> C1, #2)	wait(#2)
27	sleep	sleep
28	sleep	sleep
29	sleep	sleep
30	sleep	sleep
31	sleep	sleep
32	sleep	sleep
33	sleep	sleep
34	sleep	sleep
35	sleep	sleep
36	sleep	sleep
37	sleep	sleep
38	sleep	sleep
39	sleep	sleep
40	sleep	sleep
41	sleep	sleep
42	sleep	sleep
43	sleep	sleep
44	sleep	sleep
45	sleep	sleep
46	sleep	sleep
47	sleep	sleep
48	sleep	sleep
49	sleep	sleep
// All flows meet their deadlines
