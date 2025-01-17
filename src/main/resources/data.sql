INSERT INTO VENDOR VALUES ('MIZUHO');
INSERT INTO VENDOR VALUES ('CITI');
INSERT INTO VENDOR VALUES ('HSBC');

INSERT INTO INSTRUMENT (NAME) VALUES ('SNP500');
INSERT INTO INSTRUMENT (NAME) VALUES ('EUROSTOXX');
INSERT INTO INSTRUMENT (NAME) VALUES ('NVDA');

INSERT INTO INSTRUMENT_VENDOR_PRICE (INSTRUMENT_NAME, VENDOR_NAME, PRICE, PRICE_DATE) VALUES ('SNP500', 'MIZUHO', 100, '2024-06-01 00:00:00+00');
INSERT INTO INSTRUMENT_VENDOR_PRICE (INSTRUMENT_NAME, VENDOR_NAME, PRICE, PRICE_DATE) VALUES ('SNP500', 'CITI', 99.9, '2024-06-01 00:00:00+00');
INSERT INTO INSTRUMENT_VENDOR_PRICE (INSTRUMENT_NAME, VENDOR_NAME, PRICE, PRICE_DATE) VALUES ('SNP500', 'HSBC', 99.95, '2024-06-01 00:00:00+00');
INSERT INTO INSTRUMENT_VENDOR_PRICE (INSTRUMENT_NAME, VENDOR_NAME, PRICE, PRICE_DATE) VALUES ('NVDA', 'HSBC', 596, '2024-06-15 00:00:00+00');
INSERT INTO INSTRUMENT_VENDOR_PRICE (INSTRUMENT_NAME, VENDOR_NAME, PRICE, PRICE_DATE) VALUES ('NVDA', 'MIZUHO', 596.1, '2024-06-15 00:00:00+00');
INSERT INTO INSTRUMENT_VENDOR_PRICE (INSTRUMENT_NAME, VENDOR_NAME, PRICE, PRICE_DATE) VALUES ('EUROSTOXX', 'MIZUHO', 421, '2024-06-15 00:00:00+00');
INSERT INTO INSTRUMENT_VENDOR_PRICE (INSTRUMENT_NAME, VENDOR_NAME, PRICE, PRICE_DATE) VALUES ('EUROSTOXX', 'MIZUHO', 422, CURRENT_TIMESTAMP);
