create table if not exists DICT
(dict_lang CHAR(2) NOT NULL, dict_key VARCHAR(100) NOT NULL, dict_value TEXT NOT NULL, PRIMARY KEY (dict_lang,dict_key));
merge into DICT KEY(dict_lang, dict_key)
values
('JP', 'LOGIN', 'ログイン'),
('JP', 'LOGOUT', 'ログアウト'),
('JP', 'USERNAME', 'ユーザ名'),
('JP', 'GREETING', 'こんにちは {0} さん'),
('JP', 'USER_SETTINGS', 'ユーザ設定'),
('JP', 'LANGUAGE', '言語'),
('JP', 'LANG_JAPANESE', '日本語'),
('JP', 'LANG_ENGLISH', '英語'),
('JP', 'SAVE', '保存'),
('JP', 'BACK', '戻る');