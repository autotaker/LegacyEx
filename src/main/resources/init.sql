create table if not exists DICT
(dict_lang CHAR(2) NOT NULL,
 dict_key VARCHAR(100) NOT NULL,
 dict_value TEXT NOT NULL,
 PRIMARY KEY (dict_lang,dict_key));
create table if not exists GLOBAL_CONFIG
(config_key VARCHAR(100) PRIMARY KEY,
 config_value TEXT NOT NULL);
merge into DICT KEY(dict_lang, dict_key)
values
('JA', 'LOGIN', 'ログイン'),
('JA', 'LOGOUT', 'ログアウト'),
('JA', 'USERNAME', 'ユーザ名'),
('JA', 'GREETING', 'こんにちは {0} さん'),
('JA', 'USER_SETTINGS', 'ユーザ設定'),
('JA', 'LANGUAGE', '言語'),
('JA', 'LANG_JAPANESE', '日本語'),
('JA', 'LANG_ENGLISH', '英語'),
('JA', 'BIRTHDAY', '生年月日'),
('JA', 'SAVE', '保存'),
('JA', 'BACK', '戻る'),
('JA', 'GLOBAL_CONFIG', 'サーバ設定');
insert into GLOBAL_CONFIG
values
('DEFAULT_LANGUAGE', 'JA');