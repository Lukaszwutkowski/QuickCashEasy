-- Adding various categories simulating sections in a mini supermarket with grocery and stationery items
INSERT INTO category (name, description) VALUES
                                             ('Fruits & Vegetables', 'Fresh fruits and vegetables'),
                                             ('Dairy & Eggs', 'Milk, cheese, yogurt, eggs, and other dairy products'),
                                             ('Bakery', 'Fresh bread, pastries, and baked goods'),
                                             ('Meat & Seafood', 'Various types of fresh meat, poultry, and seafood'),
                                             ('Beverages', 'Juices, soft drinks, water, and other beverages'),
                                             ('Snacks & Sweets', 'Chips, chocolates, candies, and other snacks'),
                                             ('Frozen Foods', 'Frozen vegetables, ready meals, ice creams, etc.'),
                                             ('Household Essentials', 'Cleaning supplies, toiletries, and other home essentials'),
                                             ('Canned & Packaged Foods', 'Canned vegetables, soups, pasta, rice, and sauces'),
                                             ('Breakfast & Cereal', 'Cereals, oats, spreads, and breakfast items'),
                                             ('Stationery', 'Notebooks, pens, pencils, and school supplies'),
                                             ('Office Supplies', 'Office items like folders, staplers, and paper clips'),
                                             ('Personal Care', 'Shampoo, soap, toothpaste, and other personal care items'),
                                             ('Pet Supplies', 'Food, treats, and accessories for pets');

-- Adding sample products for the supermarket simulation with prices in NOK

-- Fruits & Vegetables (category_id = 1)
INSERT INTO PRODUCTS (barcode, name, price, category_id) VALUES
                                                            ('0100000000001', 'Apple', 10.00, 1),
                                                            ('0100000000002', 'Banana', 12.00, 1),
                                                            ('0100000000003', 'Carrot', 8.00, 1),
                                                            ('0100000000004', 'Broccoli', 15.00, 1);

-- Dairy & Eggs (category_id = 2)
INSERT INTO PRODUCTS (barcode, name, price, category_id) VALUES
                                                            ('0200000000001', 'Milk', 20.00, 2),
                                                            ('0200000000002', 'Cheese', 80.00, 2),
                                                            ('0200000000003', 'Yogurt', 15.00, 2),
                                                            ('0200000000004', 'Eggs (12 pack)', 40.00, 2);

-- Bakery (category_id = 3)
INSERT INTO PRODUCTS (barcode, name, price, category_id) VALUES
                                                            ('0300000000001', 'Bread', 25.00, 3),
                                                            ('0300000000002', 'Croissant', 20.00, 3),
                                                            ('0300000000003', 'Baguette', 22.00, 3),
                                                            ('0300000000004', 'Donut', 18.00, 3);

-- Meat & Seafood (category_id = 4)
INSERT INTO PRODUCTS (barcode, name, price, category_id) VALUES
                                                            ('0400000000001', 'Chicken Breast', 120.00, 4),
                                                            ('0400000000002', 'Salmon', 150.00, 4),
                                                            ('0400000000003', 'Beef Steak', 200.00, 4),
                                                            ('0400000000004', 'Shrimp', 180.00, 4);

-- Beverages (category_id = 5)
INSERT INTO PRODUCTS (barcode, name, price, category_id) VALUES
                                                            ('0500000000001', 'Orange Juice', 30.00, 5),
                                                            ('0500000000002', 'Soda', 25.00, 5),
                                                            ('0500000000003', 'Water (1L)', 20.00, 5),
                                                            ('0500000000004', 'Coffee', 50.00, 5);

-- Snacks & Sweets (category_id = 6)
INSERT INTO PRODUCTS (barcode, name, price, category_id) VALUES
                                                            ('0600000000001', 'Potato Chips', 35.00, 6),
                                                            ('0600000000002', 'Chocolate Bar', 20.00, 6),
                                                            ('0600000000003', 'Candy', 15.00, 6),
                                                            ('0600000000004', 'Popcorn', 25.00, 6);

-- Frozen Foods (category_id = 7)
INSERT INTO PRODUCTS (barcode, name, price, category_id) VALUES
                                                            ('0700000000001', 'Frozen Pizza', 60.00, 7),
                                                            ('0700000000002', 'Ice Cream', 50.00, 7),
                                                            ('0700000000003', 'Frozen Vegetables', 30.00, 7),
                                                            ('0700000000004', 'Frozen Fish Sticks', 40.00, 7);

-- Household Essentials (category_id = 8)
INSERT INTO PRODUCTS (barcode, name, price, category_id) VALUES
                                                            ('0800000000001', 'Dish Soap', 25.00, 8),
                                                            ('0800000000002', 'Toilet Paper (4 pack)', 40.00, 8),
                                                            ('0800000000003', 'Laundry Detergent', 100.00, 8),
                                                            ('0800000000004', 'Paper Towels', 35.00, 8);

-- Canned & Packaged Foods (category_id = 9)
INSERT INTO PRODUCTS (barcode, name, price, category_id) VALUES
                                                            ('0900000000001', 'Canned Beans', 20.00, 9),
                                                            ('0900000000002', 'Pasta', 25.00, 9),
                                                            ('0900000000003', 'Rice', 30.00, 9),
                                                            ('0900000000004', 'Tomato Sauce', 22.00, 9);

-- Breakfast & Cereal (category_id = 10)
INSERT INTO PRODUCTS (barcode, name, price, category_id) VALUES
                                                            ('1000000000011', 'Corn Flakes', 40.00, 10),
                                                            ('1000000000012', 'Oatmeal', 35.00, 10),
                                                            ('1000000000013', 'Peanut Butter', 50.00, 10),
                                                            ('1000000000014', 'Jam', 45.00, 10);

-- Stationery (category_id = 11)
INSERT INTO PRODUCTS (barcode, name, price, category_id) VALUES
                                                            ('1100000000001', 'Notebook', 20.00, 11),
                                                            ('1100000000002', 'Pen', 10.00, 11),
                                                            ('1100000000003', 'Pencil', 5.00, 11),
                                                            ('1100000000004', 'Markers', 30.00, 11);

-- Office Supplies (category_id = 12)
INSERT INTO PRODUCTS (barcode, name, price, category_id) VALUES
                                                            ('1200000000001', 'Folder', 15.00, 12),
                                                            ('1200000000002', 'Stapler', 40.00, 12),
                                                            ('1200000000003', 'Paper Clips', 10.00, 12),
                                                            ('1200000000004', 'Printer Paper (500 sheets)', 60.00, 12);

-- Personal Care (category_id = 13)
INSERT INTO PRODUCTS (barcode, name, price, category_id) VALUES
                                                            ('1300000000001', 'Shampoo', 50.00, 13),
                                                            ('1300000000002', 'Soap', 20.00, 13),
                                                            ('1300000000003', 'Toothpaste', 30.00, 13),
                                                            ('1300000000004', 'Deodorant', 40.00, 13);

-- Pet Supplies (category_id = 14)
INSERT INTO PRODUCTS (barcode, name, price, category_id) VALUES
                                                            ('1400000000001', 'Dog Food', 100.00, 14),
                                                            ('1400000000002', 'Cat Food', 90.00, 14),
                                                            ('1400000000003', 'Bird Seed', 50.00, 14),
                                                            ('1400000000004', 'Fish Flakes', 40.00, 14);
