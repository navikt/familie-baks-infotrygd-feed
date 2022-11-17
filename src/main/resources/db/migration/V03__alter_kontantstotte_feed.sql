ALTER TABLE kontantstotte_feed
    ALTER COLUMN fnr_stonadsmottaker SET NOT NULL;

ALTER TABLE kontantstotte_feed
    ALTER COLUMN dato_start_ny_ks SET NOT NULL;

ALTER TABLE kontantstotte_feed
    ALTER COLUMN dato_start_ny_ks SET DEFAULT NOW();
