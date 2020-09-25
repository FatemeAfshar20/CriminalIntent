package com.example.criminalintent.Schema;

public final class CrimeDBSchema {
    public static final String NAME = "crime.db";
    public static final int VERSION=1;

    public static final class CrimeTable {
        public static final String NAME = "CrimeTable";

        public static final class Columns {

            public static final String ID = "id";
            public static final String UUID = "uuid";
            public static final String TITLE="title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";

/*
     ------- We can declare type for columns in schema ------
         public static final class Names{
                public static final String ID="id";
                public static final String UUID="uuid";
                public static final String DATE="date";
                public static final String SOLVED="solved";
            }

            public static final class Types{
                public static final String ID="INTEGER";
                public static final String UUID="TEXT";
                public static final String DATE="TEXT";
                public static final String SOLVED="INTEGER";
            }*/
        }
    }

    public static final class UserTable {

        public static final class Columns {

        }
    }

}
