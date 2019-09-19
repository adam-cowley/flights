/*
[
  {
    origCoordinates: { x: 2.82493, y: 41.98311 },
    arrivalTimestamp: 1571985900000,
    code: 'GROEIN201910250430201910250645',
    arrivalDay: '2019102506',
    origin: 'GRO',
    destination: 'EIN',
    numStopovers: 0,
    departureDay: '2019102504',
    updateTimestamp: 1566977734364,
    searchProviders: [ 'TF' ],
    destCoordinates: { x: 5.469999, y: 51.439998 },
    provider: 'TF',
    price: 62.99,
    departureTimestamp: 1571978400000,
    departureDateTime: '201910250440',
    arrivalDateTime: '201910250645'
  }
]
*/

CREATE (a1:Airport {code:"A1"})
CREATE (a2:Airport {code:"A2"})
CREATE (a3:Airport {code:"A3"})

CREATE (s1:Segment {code: "ABCDEFGHIJKLMNOPQRSTUVWXYZ0001", provider: 'AB', price: rand() * 10, updatedAt: localdatetime() - duration('PT'+ toInteger(rand() * 5) +'H'), departureTimestamp: 1571978400000, arrivalTimestamp: 1571985900000 })
CREATE (s2:Segment {code: "ABCDEFGHIJKLMNOPQRSTUVWXYZ0002", provider: 'AB', price: rand() * 10, updatedAt: localdatetime() - duration('PT'+ toInteger(rand() * 5) +'H'), departureTimestamp: 1571978400000, arrivalTimestamp: 1571985900000})
CREATE (s3:Segment {code: "ABCDEFGHIJKLMNOPQRSTUVWXYZ0003", provider: 'AB', price: rand() * 10, updatedAt: localdatetime() - duration('PT'+ toInteger(rand() * 5) +'H'), departureTimestamp: 1571978400000, arrivalTimestamp: 1571985900000})
CREATE (s4:Segment {code: "ABCDEFGHIJKLMNOPQRSTUVWXYZ0004", provider: 'AB', price: rand() * 10, updatedAt: localdatetime() - duration('PT'+ toInteger(rand() * 5) +'H'), departureTimestamp: 1571978400000, arrivalTimestamp: 1571985900000})
CREATE (s5:Segment {code: "ABCDEFGHIJKLMNOPQRSTUVWXYZ0005", provider: 'AX', price: 22.99, updatedAt: localdatetime() - duration('PT'+ toInteger(rand() * 5) +'H'), departureTimestamp: 1571978400000, arrivalTimestamp: 1571985900000})
CREATE (s6:Segment {code: "ABCDEFGHIJKLMNOPQRSTUVWXYZ0006", provider: 'AB', price: 10.00, updatedAt: localdatetime() - duration('PT'+ toInteger(rand() * 5) +'H'), departureTimestamp: 1571978400000, arrivalTimestamp: 1571985900000})

CREATE (updatePrice:Segment     {code: "UPDATEPRICEMNOPQRSSTUVWXYZ1234", provider: 'AB', price: 10.00, updatedAt: localdatetime() - duration('P2M')})
CREATE (updateTimestamp:Segment {code: "UPDATETIMESTAMPQRSSTUVWXYZ1234", provider: 'AB', price: 10.00, updatedAt: localdatetime() - duration('PT5H')})
CREATE (ignore:Segment          {code: "IGNORETHISONEOPQRSSTUVWXYZ1234", provider: 'AB', price: 10.00, updatedAt: localdatetime() - duration('PT10M')})
