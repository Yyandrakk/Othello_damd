package es.uam.oscar_garcia.othello.database;

/**
 * Created by oscar on 22/03/17.
 */

public class RoundDataBaseSchema {
    public static final class UserTable {
        public static final String NAME = "usuarios";
        public static final class Cols {
            public static final String PLAYERUUID = "playeruuid1";
            public static final String PLAYERNAME = "playername";
            public static final String PLAYERPASSWORD = "playerpassword";
        }
    }
    public static final class RoundTable {
        public static final String NAME = "partidas";
        public static final class Cols {
            public static final String PLAYERUUID = "playeruuid2";
            public static final String ROUNDUUID = "rounduuid";
            public static final String DATE = "date";
            public static final String TITLE = "title";
            public static final String BOARD = "board";
        }
    }

}
