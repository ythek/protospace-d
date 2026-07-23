INSERT INTO affiliations (affiliation)
VALUES ('DevOps部門');

INSERT INTO positions (position)
VALUES ('社員');

INSERT INTO users (username, email, password, profile, affiliation_id, position_id)
VALUES ('testuser1', 'test@example.com', 'password123', 'プロフィール', 1, 1);

INSERT INTO prototypes (title, catchcopy, concept, image, user_id) VALUES
('ウェブアプリ1', 'テキストテキストテキスト', 'コンセプトテキスト1', 'sample1.png', 1),
('ウェブアプリ2', 'テキストテキストテキスト', 'コンセプトテキスト2', 'sample2.png', 1),
('ウェブアプリ3', 'テキストテキストテキスト', 'コンセプトテキスト3', 'sample3.png', 1);