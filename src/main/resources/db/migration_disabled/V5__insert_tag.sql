-- CATEGORY 태그 (대분류)
INSERT INTO tag (name, tag_type) VALUES
                                     ('문학', 'CATEGORY'),
                                     ('고전', 'CATEGORY'),
                                     ('과학', 'CATEGORY'),
                                     ('자기계발', 'CATEGORY'),
                                     ('역사', 'CATEGORY'),
                                     ('철학', 'CATEGORY'),
                                     ('예술', 'CATEGORY'),
                                     ('심리학', 'CATEGORY'),
                                     ('기술', 'CATEGORY'),
                                     ('사회', 'CATEGORY');

-- THEME 태그 (중분류)
INSERT INTO tag (name, tag_type) VALUES
                                     ('이해', 'THEME'),
                                     ('소통', 'THEME'),
                                     ('배움', 'THEME'),
                                     ('도전', 'THEME'),
                                     ('성장', 'THEME'),
                                     ('용기', 'THEME'),
                                     ('지혜', 'THEME'),
                                     ('열정', 'THEME'),
                                     ('여정', 'THEME'),
                                     ('치유', 'THEME');

-- EMOTION 태그 (감성 태그)
INSERT INTO tag (name, tag_type) VALUES
                                     ('희망', 'EMOTION'),
                                     ('기쁨', 'EMOTION'),
                                     ('슬픔', 'EMOTION'),
                                     ('외로움', 'EMOTION'),
                                     ('감동', 'EMOTION'),
                                     ('위로', 'EMOTION'),
                                     ('불안', 'EMOTION'),
                                     ('두려움', 'EMOTION'),
                                     ('평온', 'EMOTION'),
                                     ('사랑', 'EMOTION');