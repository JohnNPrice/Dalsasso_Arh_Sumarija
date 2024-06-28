package com.example.kys;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class Note {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "text")
    public String text;

}
