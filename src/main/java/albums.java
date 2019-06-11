import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "albums")
public class albums {
    public static final String ID_FIELD_NAME = "AlbumId";
    public static final String TITLE_FIELD_NAME = "Title";
    public static final String ARTISTID_FIELD_NAME = "ArtistId";

    @DatabaseField(columnName = ID_FIELD_NAME, canBeNull = false)
    private int AlbumId;

    @DatabaseField(columnName = TITLE_FIELD_NAME)
    private String Title;

    @DatabaseField(columnName = ARTISTID_FIELD_NAME)
    private int ArtistId;

    albums(){

    }

    public int getAlbumId(){
        return AlbumId;
    }

    public void setAlbumId(){
        this.AlbumId = AlbumId;
    }


    public String getTitle(){
        return Title;
    }
    public void setTitle(){
        this.Title = Title;
    }


    public int getArtistId(){
        return ArtistId;
    }

    public void setArtistId(){
        this.ArtistId = ArtistId;
    }

}
