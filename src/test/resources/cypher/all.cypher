
UNWIND [{code:"MAD", properties:{coordinates:point({x: -3.70379, y: 40.416775, crs: 'wgs-84'})}}, {code:"IBZ", properties:{coordinates:point({x: 1.42097, y: 38.906797, crs: 'wgs-84'})}}, {code:"JFK", properties:{coordinates:point({x: -73.978801, y: 40.764811, crs: 'wgs-84'})}}] as row
CREATE (n:Airport{code: row.code}) SET n += row.properties WITH distinct true AS next
UNWIND [{code:"MAD-20190922-IBZ", properties:{}}, {code:"MAD-20190922-JFK", properties:{}}, {code:"IBZ-20190922-MAD", properties:{}}] as row
CREATE (n:AirportDestination{code: row.code}) SET n += row.properties WITH distinct true AS next
UNWIND [{code:"MADIBZ201909221300201909221600", properties:{arrives:datetime('2019-09-22T16:05:00Z'), price:157.3, numStopovers:1, departs:datetime('2019-09-22T13:00:00Z')}}, {code:"MADIBZ201909221500201909230600", properties:{arrives:datetime('2019-09-23T06:00:00Z'), price:164.78, numStopovers:1, departs:datetime('2019-09-22T15:00:00Z')}}, {code:"MADJFK201909221815201909230230", properties:{arrives:datetime('2019-09-23T02:30:00Z'), price:289.25, numStopovers:0, departs:datetime('2019-09-22T18:25:00Z')}}, {code:"MADIBZ201909220500201909221645", properties:{arrives:datetime('2019-09-22T16:55:00Z'), price:134.3, numStopovers:1, departs:datetime('2019-09-22T05:00:00Z')}}, {code:"MADIBZ201909221400201909221700", properties:{arrives:datetime('2019-09-22T17:00:00Z'), price:277.97, numStopovers:1, departs:datetime('2019-09-22T14:10:00Z')}}, {code:"MADIBZ201909222000201909222100", properties:{arrives:datetime('2019-09-22T21:10:00Z'), price:60.39, numStopovers:0, departs:datetime('2019-09-22T20:00:00Z')}}, {code:"MADIBZ201909220615201909221200", properties:{arrives:datetime('2019-09-22T12:05:00Z'), price:71.19, numStopovers:1, departs:datetime('2019-09-22T06:20:00Z')}}, {code:"MADIBZ201909220500201909221915", properties:{arrives:datetime('2019-09-22T19:25:00Z'), price:104.78, numStopovers:1, departs:datetime('2019-09-22T05:00:00Z')}}, {code:"MADIBZ201909220615201909221545", properties:{arrives:datetime('2019-09-22T15:50:00Z'), price:106.19, numStopovers:1, departs:datetime('2019-09-22T06:20:00Z')}}, {code:"MADIBZ201909220445201909220615", properties:{arrives:datetime('2019-09-22T06:15:00Z'), price:90.39, numStopovers:0, departs:datetime('2019-09-22T04:55:00Z')}}, {code:"MADIBZ201909221300201909230600", properties:{arrives:datetime('2019-09-23T06:00:00Z'), price:164.78, numStopovers:1, departs:datetime('2019-09-22T13:00:00Z')}}, {code:"MADIBZ201909221045201909221200", properties:{arrives:datetime('2019-09-22T12:05:00Z'), price:105.39, numStopovers:0, departs:datetime('2019-09-22T10:50:00Z')}}, {code:"MADIBZ201909220500201909221300", properties:{arrives:datetime('2019-09-22T13:05:00Z'), price:134.3, numStopovers:1, departs:datetime('2019-09-22T05:00:00Z')}}, {code:"MADIBZ201909221300201909221715", properties:{arrives:datetime('2019-09-22T17:15:00Z'), price:198.19, numStopovers:1, departs:datetime('2019-09-22T13:00:00Z')}}, {code:"MADIBZ201909221300201909222000", properties:{arrives:datetime('2019-09-22T20:05:00Z'), price:147.3, numStopovers:1, departs:datetime('2019-09-22T13:00:00Z')}}, {code:"MADIBZ201909220815201909221500", properties:{arrives:datetime('2019-09-22T15:00:00Z'), price:131.19, numStopovers:1, departs:datetime('2019-09-22T08:15:00Z')}}, {code:"MADIBZ201909220615201909221300", properties:{arrives:datetime('2019-09-22T13:10:00Z'), price:100.19, numStopovers:1, departs:datetime('2019-09-22T06:20:00Z')}}, {code:"MADIBZ201909221000201909221600", properties:{arrives:datetime('2019-09-22T16:05:00Z'), price:157.3, numStopovers:1, departs:datetime('2019-09-22T10:00:00Z')}}, {code:"MADIBZ201909221500201909222000", properties:{arrives:datetime('2019-09-22T20:05:00Z'), price:157.3, numStopovers:1, departs:datetime('2019-09-22T15:00:00Z')}}, {code:"MADIBZ201909220615201909220730", properties:{arrives:datetime('2019-09-22T07:40:00Z'), price:93.39, numStopovers:0, departs:datetime('2019-09-22T06:25:00Z')}}] as row
CREATE (n:Segment{code: row.code}) SET n += row.properties WITH distinct true AS next
UNWIND [{code:"MADIBZ201909220500201909222045", properties:{arrives:datetime('2019-09-22T20:55:00Z'), price:96.78, numStopovers:1, departs:datetime('2019-09-22T05:00:00Z')}}, {code:"MADIBZ201909220700201909221600", properties:{arrives:datetime('2019-09-22T16:05:00Z'), price:147.3, numStopovers:1, departs:datetime('2019-09-22T07:00:00Z')}}, {code:"MADIBZ201909220500201909221145", properties:{arrives:datetime('2019-09-22T11:45:00Z'), price:134.3, numStopovers:1, departs:datetime('2019-09-22T05:00:00Z')}}, {code:"MADIBZ201909221700201909222045", properties:{arrives:datetime('2019-09-22T20:55:00Z'), price:166.3, numStopovers:1, departs:datetime('2019-09-22T17:00:00Z')}}, {code:"MADIBZ201909220615201909221730", properties:{arrives:datetime('2019-09-22T17:35:00Z'), price:131.19, numStopovers:1, departs:datetime('2019-09-22T06:20:00Z')}}, {code:"IBZMAD201909221245201909221400", properties:{arrives:datetime('2019-09-22T14:05:00Z'), price:126.55, numStopovers:0, departs:datetime('2019-09-22T12:45:00Z')}}, {code:"MADIBZ201909220700201909222000", properties:{arrives:datetime('2019-09-22T20:05:00Z'), price:147.3, numStopovers:1, departs:datetime('2019-09-22T07:00:00Z')}}, {code:"MADIBZ201909221500201909230815", properties:{arrives:datetime('2019-09-23T08:20:00Z'), price:164.78, numStopovers:1, departs:datetime('2019-09-22T15:00:00Z')}}, {code:"MADIBZ201909220600201909221000", properties:{arrives:datetime('2019-09-22T10:10:00Z'), price:102.97, numStopovers:1, departs:datetime('2019-09-22T06:10:00Z')}}, {code:"MADIBZ201909221615201909222000", properties:{arrives:datetime('2019-09-22T20:05:00Z'), price:174.3, numStopovers:1, departs:datetime('2019-09-22T16:15:00Z')}}, {code:"MADIBZ201909221300201909221545", properties:{arrives:datetime('2019-09-22T15:45:00Z'), price:198.19, numStopovers:1, departs:datetime('2019-09-22T13:00:00Z')}}, {code:"MADIBZ201909221000201909222000", properties:{arrives:datetime('2019-09-22T20:05:00Z'), price:157.3, numStopovers:1, departs:datetime('2019-09-22T10:00:00Z')}}, {code:"MADIBZ201909221145201909221500", properties:{arrives:datetime('2019-09-22T15:00:00Z'), price:141.19, numStopovers:1, departs:datetime('2019-09-22T11:50:00Z')}}, {code:"MADJFK201909220600201909230130", properties:{arrives:datetime('2019-09-23T01:30:00Z'), price:247.14, numStopovers:1, departs:datetime('2019-09-22T06:00:00Z')}}, {code:"MADIBZ201909220500201909220945", properties:{arrives:datetime('2019-09-22T09:50:00Z'), price:134.3, numStopovers:1, departs:datetime('2019-09-22T05:00:00Z')}}, {code:"IBZMAD201909220630201909220745", properties:{arrives:datetime('2019-09-22T07:55:00Z'), price:105.19, numStopovers:0, departs:datetime('2019-09-22T06:35:00Z')}}, {code:"MADIBZ201909220615201909221500", properties:{arrives:datetime('2019-09-22T15:00:00Z'), price:71.19, numStopovers:1, departs:datetime('2019-09-22T06:20:00Z')}}, {code:"MADIBZ201909220500201909221600", properties:{arrives:datetime('2019-09-22T16:05:00Z'), price:134.3, numStopovers:1, departs:datetime('2019-09-22T05:00:00Z')}}, {code:"MADIBZ201909222100201909231515", properties:{arrives:datetime('2019-09-23T15:25:00Z'), price:106.89, numStopovers:1, departs:datetime('2019-09-22T21:10:00Z')}}, {code:"MADIBZ201909221300201909221915", properties:{arrives:datetime('2019-09-22T19:25:00Z'), price:147.3, numStopovers:1, departs:datetime('2019-09-22T13:00:00Z')}}] as row
CREATE (n:Segment{code: row.code}) SET n += row.properties WITH distinct true AS next
UNWIND [{code:"IBZMAD201909222000201909222130", properties:{arrives:datetime('2019-09-22T21:30:00Z'), price:183.16, numStopovers:0, departs:datetime('2019-09-22T20:10:00Z')}}, {code:"MADIBZ201909221415201909221530", properties:{arrives:datetime('2019-09-22T15:40:00Z'), price:161.39, numStopovers:0, departs:datetime('2019-09-22T14:25:00Z')}}, {code:"MADIBZ201909220600201909221700", properties:{arrives:datetime('2019-09-22T17:00:00Z'), price:112.97, numStopovers:1, departs:datetime('2019-09-22T06:10:00Z')}}, {code:"MADIBZ201909221500201909221915", properties:{arrives:datetime('2019-09-22T19:25:00Z'), price:157.3, numStopovers:1, departs:datetime('2019-09-22T15:00:00Z')}}, {code:"MADIBZ201909221300201909221645", properties:{arrives:datetime('2019-09-22T16:55:00Z'), price:147.3, numStopovers:1, departs:datetime('2019-09-22T13:00:00Z')}}, {code:"MADIBZ201909220615201909220945", properties:{arrives:datetime('2019-09-22T09:45:00Z'), price:71.19, numStopovers:1, departs:datetime('2019-09-22T06:20:00Z')}}, {code:"MADIBZ201909220700201909221300", properties:{arrives:datetime('2019-09-22T13:05:00Z'), price:147.3, numStopovers:1, departs:datetime('2019-09-22T07:00:00Z')}}, {code:"MADIBZ201909221700201909221945", properties:{arrives:datetime('2019-09-22T19:55:00Z'), price:448.19, numStopovers:1, departs:datetime('2019-09-22T17:00:00Z')}}, {code:"MADIBZ201909221330201909221715", properties:{arrives:datetime('2019-09-22T17:15:00Z'), price:254.19, numStopovers:1, departs:datetime('2019-09-22T13:40:00Z')}}, {code:"MADIBZ201909221000201909221645", properties:{arrives:datetime('2019-09-22T16:55:00Z'), price:157.3, numStopovers:1, departs:datetime('2019-09-22T10:00:00Z')}}, {code:"MADJFK201909220600201909230000", properties:{arrives:datetime('2019-09-23T00:05:00Z'), price:255.42, numStopovers:1, departs:datetime('2019-09-22T06:00:00Z')}}, {code:"MADIBZ201909220530201909221000", properties:{arrives:datetime('2019-09-22T10:10:00Z'), price:90.97, numStopovers:1, departs:datetime('2019-09-22T05:30:00Z')}}, {code:"MADIBZ201909220700201909221645", properties:{arrives:datetime('2019-09-22T16:55:00Z'), price:147.3, numStopovers:1, departs:datetime('2019-09-22T07:00:00Z')}}, {code:"MADIBZ201909220700201909221915", properties:{arrives:datetime('2019-09-22T19:25:00Z'), price:147.3, numStopovers:1, departs:datetime('2019-09-22T07:00:00Z')}}, {code:"MADIBZ201909220815201909221200", properties:{arrives:datetime('2019-09-22T12:05:00Z'), price:131.19, numStopovers:1, departs:datetime('2019-09-22T08:15:00Z')}}, {code:"MADIBZ201909221500201909221730", properties:{arrives:datetime('2019-09-22T17:35:00Z'), price:499.19, numStopovers:1, departs:datetime('2019-09-22T15:00:00Z')}}, {code:"MADIBZ201909221500201909222045", properties:{arrives:datetime('2019-09-22T20:55:00Z'), price:153.3, numStopovers:1, departs:datetime('2019-09-22T15:00:00Z')}}, {code:"MADIBZ201909220500201909222000", properties:{arrives:datetime('2019-09-22T20:05:00Z'), price:104.78, numStopovers:1, departs:datetime('2019-09-22T05:00:00Z')}}, {code:"MADIBZ201909220500201909220815", properties:{arrives:datetime('2019-09-22T08:20:00Z'), price:134.3, numStopovers:1, departs:datetime('2019-09-22T05:00:00Z')}}, {code:"MADIBZ201909220615201909221945", properties:{arrives:datetime('2019-09-22T19:55:00Z'), price:131.19, numStopovers:1, departs:datetime('2019-09-22T06:20:00Z')}}] as row
CREATE (n:Segment{code: row.code}) SET n += row.properties WITH distinct true AS next
UNWIND [{code:"MADIBZ201909221300201909222045", properties:{arrives:datetime('2019-09-22T20:55:00Z'), price:146.3, numStopovers:1, departs:datetime('2019-09-22T13:00:00Z')}}, {code:"MADIBZ201909221000201909221915", properties:{arrives:datetime('2019-09-22T19:25:00Z'), price:157.3, numStopovers:1, departs:datetime('2019-09-22T10:00:00Z')}}, {code:"MADIBZ201909221000201909221300", properties:{arrives:datetime('2019-09-22T13:05:00Z'), price:157.3, numStopovers:1, departs:datetime('2019-09-22T10:00:00Z')}}, {code:"MADIBZ201909221500201909230745", properties:{arrives:datetime('2019-09-23T07:50:00Z'), price:164.78, numStopovers:1, departs:datetime('2019-09-22T15:00:00Z')}}, {code:"MADIBZ201909221615201909221915", properties:{arrives:datetime('2019-09-22T19:25:00Z'), price:192.3, numStopovers:1, departs:datetime('2019-09-22T16:15:00Z')}}, {code:"MADIBZ201909221900201909222015", properties:{arrives:datetime('2019-09-22T20:25:00Z'), price:105.39, numStopovers:0, departs:datetime('2019-09-22T19:05:00Z')}}, {code:"MADIBZ201909221700201909222000", properties:{arrives:datetime('2019-09-22T20:05:00Z'), price:192.3, numStopovers:1, departs:datetime('2019-09-22T17:00:00Z')}}, {code:"MADIBZ201909220700201909221145", properties:{arrives:datetime('2019-09-22T11:45:00Z'), price:147.3, numStopovers:1, departs:datetime('2019-09-22T07:00:00Z')}}, {code:"MADIBZ201909220700201909222045", properties:{arrives:datetime('2019-09-22T20:55:00Z'), price:146.3, numStopovers:1, departs:datetime('2019-09-22T07:00:00Z')}}, {code:"MADIBZ201909221615201909222045", properties:{arrives:datetime('2019-09-22T20:55:00Z'), price:166.3, numStopovers:1, departs:datetime('2019-09-22T16:15:00Z')}}, {code:"MADIBZ201909220515201909220915", properties:{arrives:datetime('2019-09-22T09:15:00Z'), price:198.19, numStopovers:1, departs:datetime('2019-09-22T05:25:00Z')}}, {code:"MADIBZ201909220815201909221300", properties:{arrives:datetime('2019-09-22T13:10:00Z'), price:131.19, numStopovers:1, departs:datetime('2019-09-22T08:15:00Z')}}, {code:"MADIBZ201909222100201909230545", properties:{arrives:datetime('2019-09-23T05:45:00Z'), price:127.97, numStopovers:1, departs:datetime('2019-09-22T21:10:00Z')}}, {code:"MADJFK201909220830201909222245", properties:{arrives:datetime('2019-09-22T22:55:00Z'), price:662.09, numStopovers:1, departs:datetime('2019-09-22T08:30:00Z')}}, {code:"MADIBZ201909221145201909221545", properties:{arrives:datetime('2019-09-22T15:50:00Z'), price:200.19, numStopovers:1, departs:datetime('2019-09-22T11:50:00Z')}}, {code:"MADIBZ201909221000201909222045", properties:{arrives:datetime('2019-09-22T20:55:00Z'), price:153.3, numStopovers:1, departs:datetime('2019-09-22T10:00:00Z')}}, {code:"MADIBZ201909220530201909221700", properties:{arrives:datetime('2019-09-22T17:00:00Z'), price:100.97, numStopovers:1, departs:datetime('2019-09-22T05:30:00Z')}}, {code:"MADIBZ201909220915201909221200", properties:{arrives:datetime('2019-09-22T12:05:00Z'), price:226.19, numStopovers:1, departs:datetime('2019-09-22T09:15:00Z')}}, {code:"MADIBZ201909221715201909221830", properties:{arrives:datetime('2019-09-22T18:40:00Z'), price:59.39, numStopovers:0, departs:datetime('2019-09-22T17:25:00Z')}}] as row
CREATE (n:Segment{code: row.code}) SET n += row.properties WITH distinct true AS next
UNWIND [{code:"MAD-20190922", properties:{}}, {code:"IBZ-20190922", properties:{}}] as row
CREATE (n:AirportDay{code: row.code}) SET n += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MADIBZ201909220445201909220615"}, end: {code:"IBZ"}, properties:{}}] as row
MATCH (start:Segment{code: row.start.code})
MATCH (end:Airport{code: row.end.code})
CREATE (start)-[r:`2019092206`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MAD"}, end: {code:"JFK"}, properties:{duration:duration('PT8H5M'), distance:5769996.702142081, price:289.25}}, {start: {code:"MAD"}, end: {code:"IBZ"}, properties:{duration:duration('PT1H10M'), distance:470151.03052573017, price:59.39}}, {start: {code:"IBZ"}, end: {code:"MAD"}, properties:{duration:duration('PT1H20M'), distance:470151.03052573017, price:105.19}}] as row
MATCH (start:Airport{code: row.start.code})
MATCH (end:Airport{code: row.end.code})
CREATE (start)-[r:FLIES_TO]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221000201909221600"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221000201909222000"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221000201909221645"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221000201909221915"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221000201909221300"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221000201909222045"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221045201909221200"}, properties:{}}] as row
MATCH (start:AirportDestination{code: row.start.code})
MATCH (end:Segment{code: row.end.code})
CREATE (start)-[r:`2019092210`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221145201909221500"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221145201909221545"}, properties:{}}] as row
MATCH (start:AirportDestination{code: row.start.code})
MATCH (end:Segment{code: row.end.code})
CREATE (start)-[r:`2019092211`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221700201909222045"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221700201909221945"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221700201909222000"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221715201909221830"}, properties:{}}] as row
MATCH (start:AirportDestination{code: row.start.code})
MATCH (end:Segment{code: row.end.code})
CREATE (start)-[r:`2019092217`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MADIBZ201909220615201909220730"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"IBZMAD201909220630201909220745"}, end: {code:"MAD"}, properties:{}}] as row
MATCH (start:Segment{code: row.start.code})
MATCH (end:Airport{code: row.end.code})
CREATE (start)-[r:`2019092207`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MADIBZ201909220500201909220815"}, end: {code:"IBZ"}, properties:{}}] as row
MATCH (start:Segment{code: row.start.code})
MATCH (end:Airport{code: row.end.code})
CREATE (start)-[r:`2019092208`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220700201909221600"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220700201909222000"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220700201909221300"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220700201909221645"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220700201909221915"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220700201909221145"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220700201909222045"}, properties:{}}] as row
MATCH (start:AirportDestination{code: row.start.code})
MATCH (end:Segment{code: row.end.code})
CREATE (start)-[r:`2019092207`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220500201909222045"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220500201909221145"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220500201909220945"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220500201909221600"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220530201909221000"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220500201909222000"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220500201909220815"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220515201909220915"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220530201909221700"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220500201909221645"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220500201909221915"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220500201909221300"}, properties:{}}] as row
MATCH (start:AirportDestination{code: row.start.code})
MATCH (end:Segment{code: row.end.code})
CREATE (start)-[r:`2019092205`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220815201909221500"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220815201909221200"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220815201909221300"}, properties:{}}, {start: {code:"MAD-20190922-JFK"}, end: {code:"MADJFK201909220830201909222245"}, properties:{}}] as row
MATCH (start:AirportDestination{code: row.start.code})
MATCH (end:Segment{code: row.end.code})
CREATE (start)-[r:`2019092208`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220445201909220615"}, properties:{}}] as row
MATCH (start:AirportDestination{code: row.start.code})
MATCH (end:Segment{code: row.end.code})
CREATE (start)-[r:`2019092204`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220915201909221200"}, properties:{}}] as row
MATCH (start:AirportDestination{code: row.start.code})
MATCH (end:Segment{code: row.end.code})
CREATE (start)-[r:`2019092209`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220615201909221300"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220615201909220730"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220615201909221730"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220600201909221000"}, properties:{}}, {start: {code:"MAD-20190922-JFK"}, end: {code:"MADJFK201909220600201909230130"}, properties:{}}, {start: {code:"IBZ-20190922-MAD"}, end: {code:"IBZMAD201909220630201909220745"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220615201909221500"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220600201909221700"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220615201909220945"}, properties:{}}, {start: {code:"MAD-20190922-JFK"}, end: {code:"MADJFK201909220600201909230000"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220615201909221945"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220615201909221200"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909220615201909221545"}, properties:{}}] as row
MATCH (start:AirportDestination{code: row.start.code})
MATCH (end:Segment{code: row.end.code})
CREATE (start)-[r:`2019092206`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MADIBZ201909222100201909231515"}, end: {code:"IBZ"}, properties:{}}] as row
MATCH (start:Segment{code: row.start.code})
MATCH (end:Airport{code: row.end.code})
CREATE (start)-[r:`2019092315`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MADIBZ201909221500201909230815"}, end: {code:"IBZ"}, properties:{}}] as row
MATCH (start:Segment{code: row.start.code})
MATCH (end:Airport{code: row.end.code})
CREATE (start)-[r:`2019092308`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"IBZ-20190922"}, end: {code:"IBZ-20190922-MAD"}, properties:{}}] as row
MATCH (start:AirportDay{code: row.start.code})
MATCH (end:AirportDestination{code: row.end.code})
CREATE (start)-[r:MAD]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MADIBZ201909222100201909230545"}, end: {code:"IBZ"}, properties:{}}] as row
MATCH (start:Segment{code: row.start.code})
MATCH (end:Airport{code: row.end.code})
CREATE (start)-[r:`2019092305`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MADIBZ201909222000201909222100"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"IBZMAD201909222000201909222130"}, end: {code:"MAD"}, properties:{}}] as row
MATCH (start:Segment{code: row.start.code})
MATCH (end:Airport{code: row.end.code})
CREATE (start)-[r:`2019092221`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MADJFK201909220830201909222245"}, end: {code:"JFK"}, properties:{}}] as row
MATCH (start:Segment{code: row.start.code})
MATCH (end:Airport{code: row.end.code})
CREATE (start)-[r:`2019092222`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MADJFK201909221815201909230230"}, end: {code:"JFK"}, properties:{}}] as row
MATCH (start:Segment{code: row.start.code})
MATCH (end:Airport{code: row.end.code})
CREATE (start)-[r:`2019092302`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MADJFK201909220600201909230000"}, end: {code:"JFK"}, properties:{}}] as row
MATCH (start:Segment{code: row.start.code})
MATCH (end:Airport{code: row.end.code})
CREATE (start)-[r:`2019092300`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MAD-20190922"}, end: {code:"MAD-20190922-IBZ"}, properties:{}}] as row
MATCH (start:AirportDay{code: row.start.code})
MATCH (end:AirportDestination{code: row.end.code})
CREATE (start)-[r:IBZ]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MADJFK201909220600201909230130"}, end: {code:"JFK"}, properties:{}}] as row
MATCH (start:Segment{code: row.start.code})
MATCH (end:Airport{code: row.end.code})
CREATE (start)-[r:`2019092301`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MADIBZ201909221300201909230600"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909221500201909230600"}, end: {code:"IBZ"}, properties:{}}] as row
MATCH (start:Segment{code: row.start.code})
MATCH (end:Airport{code: row.end.code})
CREATE (start)-[r:`2019092306`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MADIBZ201909221500201909230745"}, end: {code:"IBZ"}, properties:{}}] as row
MATCH (start:Segment{code: row.start.code})
MATCH (end:Airport{code: row.end.code})
CREATE (start)-[r:`2019092307`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MADIBZ201909221300201909222000"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909221500201909222000"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909220500201909222045"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909221700201909222045"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909220700201909222000"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909221615201909222000"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909221000201909222000"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909221500201909222045"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909220500201909222000"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909221300201909222045"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909221900201909222015"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909221700201909222000"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909220700201909222045"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909221615201909222045"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909221000201909222045"}, end: {code:"IBZ"}, properties:{}}] as row
MATCH (start:Segment{code: row.start.code})
MATCH (end:Airport{code: row.end.code})
CREATE (start)-[r:`2019092220`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MADIBZ201909221400201909221700"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909221300201909221715"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909220615201909221730"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909220600201909221700"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909221330201909221715"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909221500201909221730"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909220530201909221700"}, end: {code:"IBZ"}, properties:{}}] as row
MATCH (start:Segment{code: row.start.code})
MATCH (end:Airport{code: row.end.code})
CREATE (start)-[r:`2019092217`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"IBZ-20190922-MAD"}, end: {code:"IBZMAD201909222000201909222130"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909222000201909222100"}, properties:{}}] as row
MATCH (start:AirportDestination{code: row.start.code})
MATCH (end:Segment{code: row.end.code})
CREATE (start)-[r:`2019092220`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MADIBZ201909220600201909221000"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909220530201909221000"}, end: {code:"IBZ"}, properties:{}}] as row
MATCH (start:Segment{code: row.start.code})
MATCH (end:Airport{code: row.end.code})
CREATE (start)-[r:`2019092210`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MADIBZ201909220500201909221145"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909220700201909221145"}, end: {code:"IBZ"}, properties:{}}] as row
MATCH (start:Segment{code: row.start.code})
MATCH (end:Airport{code: row.end.code})
CREATE (start)-[r:`2019092211`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909222100201909231515"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909222100201909230545"}, properties:{}}] as row
MATCH (start:AirportDestination{code: row.start.code})
MATCH (end:Segment{code: row.end.code})
CREATE (start)-[r:`2019092221`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"IBZMAD201909221245201909221400"}, end: {code:"MAD"}, properties:{}}] as row
MATCH (start:Segment{code: row.start.code})
MATCH (end:Airport{code: row.end.code})
CREATE (start)-[r:`2019092214`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MADIBZ201909220615201909221545"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909220815201909221500"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909221300201909221545"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909221145201909221500"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909220615201909221500"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909221415201909221530"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909221145201909221545"}, end: {code:"IBZ"}, properties:{}}] as row
MATCH (start:Segment{code: row.start.code})
MATCH (end:Airport{code: row.end.code})
CREATE (start)-[r:`2019092215`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MADIBZ201909220615201909221200"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909221045201909221200"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909220815201909221200"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909220915201909221200"}, end: {code:"IBZ"}, properties:{}}] as row
MATCH (start:Segment{code: row.start.code})
MATCH (end:Airport{code: row.end.code})
CREATE (start)-[r:`2019092212`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MADIBZ201909220500201909221300"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909220615201909221300"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909220700201909221300"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909221000201909221300"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909220815201909221300"}, end: {code:"IBZ"}, properties:{}}] as row
MATCH (start:Segment{code: row.start.code})
MATCH (end:Airport{code: row.end.code})
CREATE (start)-[r:`2019092213`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MADIBZ201909221715201909221830"}, end: {code:"IBZ"}, properties:{}}] as row
MATCH (start:Segment{code: row.start.code})
MATCH (end:Airport{code: row.end.code})
CREATE (start)-[r:`2019092218`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MAD-20190922"}, end: {code:"MAD-20190922-JFK"}, properties:{}}] as row
MATCH (start:AirportDay{code: row.start.code})
MATCH (end:AirportDestination{code: row.end.code})
CREATE (start)-[r:JFK]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MADIBZ201909220500201909221915"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909221300201909221915"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909221500201909221915"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909221700201909221945"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909220700201909221915"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909220615201909221945"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909221000201909221915"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909221615201909221915"}, end: {code:"IBZ"}, properties:{}}] as row
MATCH (start:Segment{code: row.start.code})
MATCH (end:Airport{code: row.end.code})
CREATE (start)-[r:`2019092219`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MADIBZ201909221000201909221600"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909220700201909221600"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909220500201909221600"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909221300201909221645"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909221000201909221645"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909220700201909221645"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909221300201909221600"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909220500201909221645"}, end: {code:"IBZ"}, properties:{}}] as row
MATCH (start:Segment{code: row.start.code})
MATCH (end:Airport{code: row.end.code})
CREATE (start)-[r:`2019092216`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MAD-20190922-JFK"}, end: {code:"MADJFK201909221815201909230230"}, properties:{}}] as row
MATCH (start:AirportDestination{code: row.start.code})
MATCH (end:Segment{code: row.end.code})
CREATE (start)-[r:`2019092218`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221300201909222000"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221300201909221545"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221300201909221915"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221300201909221645"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221330201909221715"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221300201909222045"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221300201909221600"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221300201909230600"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221300201909221715"}, properties:{}}] as row
MATCH (start:AirportDestination{code: row.start.code})
MATCH (end:Segment{code: row.end.code})
CREATE (start)-[r:`2019092213`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221615201909222000"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221615201909221915"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221615201909222045"}, properties:{}}] as row
MATCH (start:AirportDestination{code: row.start.code})
MATCH (end:Segment{code: row.end.code})
CREATE (start)-[r:`2019092216`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MADIBZ201909220500201909220945"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909220615201909220945"}, end: {code:"IBZ"}, properties:{}}, {start: {code:"MADIBZ201909220515201909220915"}, end: {code:"IBZ"}, properties:{}}] as row
MATCH (start:Segment{code: row.start.code})
MATCH (end:Airport{code: row.end.code})
CREATE (start)-[r:`2019092209`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221900201909222015"}, properties:{}}] as row
MATCH (start:AirportDestination{code: row.start.code})
MATCH (end:Segment{code: row.end.code})
CREATE (start)-[r:`2019092219`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221415201909221530"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221400201909221700"}, properties:{}}] as row
MATCH (start:AirportDestination{code: row.start.code})
MATCH (end:Segment{code: row.end.code})
CREATE (start)-[r:`2019092214`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"IBZ"}, end: {code:"IBZ-20190922"}, properties:{}}, {start: {code:"MAD"}, end: {code:"MAD-20190922"}, properties:{}}] as row
MATCH (start:Airport{code: row.start.code})
MATCH (end:AirportDay{code: row.end.code})
CREATE (start)-[r:`20190922`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"IBZ-20190922-MAD"}, end: {code:"IBZMAD201909221245201909221400"}, properties:{}}] as row
MATCH (start:AirportDestination{code: row.start.code})
MATCH (end:Segment{code: row.end.code})
CREATE (start)-[r:`2019092212`]->(end) SET r += row.properties WITH distinct true AS next
UNWIND [{start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221500201909222000"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221500201909230815"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221500201909221915"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221500201909221730"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221500201909222045"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221500201909230745"}, properties:{}}, {start: {code:"MAD-20190922-IBZ"}, end: {code:"MADIBZ201909221500201909230600"}, properties:{}}] as row
MATCH (start:AirportDestination{code: row.start.code})
MATCH (end:Segment{code: row.end.code})
CREATE (start)-[r:`2019092215`]->(end) SET r += row.properties
