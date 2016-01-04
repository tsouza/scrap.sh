package sh.scrap.scraplet.store;

import java.io.*;

public class DataScrapperCacheKey implements Externalizable {

    public String apiKey;
    public String name;

    public DataScrapperCacheKey(String apiKey, String name) {
        this.apiKey = apiKey;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataScrapperCacheKey that = (DataScrapperCacheKey) o;

        if (!apiKey.equals(that.apiKey)) return false;
        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        int result = apiKey.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeChars(apiKey);
        out.writeChar('\n');
        out.writeChars(name);
        out.writeChar('\n');
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        apiKey = in.readLine();
        name = in.readLine();
    }
}
