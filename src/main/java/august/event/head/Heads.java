package august.event.head;

import august.event.Main;
import org.bukkit.inventory.ItemStack;

public enum Heads {
    Open("Y2JlNjNmY2FhMDIwM2Y4MDQ2ZmI5NjM3NWQwNzlhNWMwOWI0MTYxZDAxMTlkNzE5NDU2NmU2ODljYWIxOGY2OCJ9fX0=","open"),
    kemerdekaan_points("OGE4ZjVmMjAyMzA0M2UyYTI4NTA2NDNlMGEzYWI2MTgyYTAzN2Y3ZWZlNTljNzZkMDhhZjdhNDVlMGRhZDczOCJ9fX0=", "kemerdekaan_points"),
    random_peso("ODM4MWM1MjlkNTJlMDNjZDc0YzNiZjM4YmI2YmEzZmRlMTMzN2FlOWJmNTAzMzJmYWE4ODllMGEyOGU4MDgxZiJ9fX0=", "random_peso"),
    rotten_box("ZjNlMjQ1N2VhNTMwODhiMWZiZDAyZTI0NzY5NmEzOWVlYjdiN2ZjNDRhZDU5MTI3N2I5ZTdiOTdhZjRjY2E1In19fQ==", "rotten_box"),
    steel_shard("YTAyZjRhOWQxODJjYzYyNTkyNWU1ZGVhN2UyOWQxM2E5NDhhZDY0ZmY0Mjg3MTc4YWYyM2EwNDI4ZTFmYTU0ZSJ9fX0=", "steel_shard"),
    patriot_badge("ZjcyMDNkMmYwOGE3ZDJiN2M5NTUzMjg2NWViMmI1OTNlNTI2ZGRiOTJmZDVjY2E5ZTdkN2I2MjJiYzUzOTkwNiJ9fX0=", "patriot_badge"),
    m1_garand_part("ZDZiMTNkNjlkMTkyOWRjZjhlZGY5OWYzOTAxNDE1MjE3YzZhNTY3ZDNhNmVhZDEyZjc1YTRkZTNlZDgzNWU4NSJ9fX0=", "m1_garand_part"),
    proclamation_paper("ZmM2NmRiZDhmYzM4MDU4ZTJjYmZkYjQ4OTQ2NDExMGUyYzI4Yjg1YjRhMDdlY2NhOWEwZjQwMmUyNTA5NDk4MyJ9fX0=", "proclamation_paper"),
    sickle_handle("NTdkZjBlN2ZmZTZiZTUzMzZmZjEzNzNjMzEzYTFmNWMxOWFkODQ1Y2RiODkwMjE1YzgzYmZhYjlmOGQ5ODliOSJ9fX0=", "sickle_handle");

    private final ItemStack item;
    private final String idTag;
    private final String url;
    public String prefix = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv";

    Heads(String texture, String id){
        item = Main.createSkull(prefix + texture, id);
        idTag = id;
        url = prefix + texture;
    }
    public String getUrl(){
        return url;
    }
    public ItemStack getItemStack() {
        return item;
    }
    public String getName() {
        return idTag;
    }
}

