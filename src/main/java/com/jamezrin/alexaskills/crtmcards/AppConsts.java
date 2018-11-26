package com.jamezrin.alexaskills.crtmcards;

public class AppConsts {
    public static final String CRTM_BASE_URI = "https://www.tarjetatransportepublico.es";
    public static final String CRTM_QUERY_URI = CRTM_BASE_URI + "/CRTM-ABONOS/consultaSaldo.aspx";
    public static final String CRTM_USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36";
    public static final String SKILL_ID = "amzn1.ask.skill.def1ad6d-f8e8-49f0-8d9f-6558599af1eb";
    public static final String SKILL_CARD_TITLE = "Tarjetas CRTM";
    public static final String VIEW_STATE = makeDefaultViewState();

    public static String makeDefaultViewState() {
        return "/wEPDwUJNzkyNzg1MDMyD2QWAmYPZBYCAgMPZBYEAgMPDxYCHgRUZXh0BSBTw6FiYWRvLCAxMCBkZSBub3ZpZW1icm" +
                "UgZGUgMjAxOGRkAgUPZBYGAgEPDxYCHwAFCjEwLzExLzIwMThkZAIDDw8WAh8ABQUxNDozMWRkAgUPZBYCAgMPZBYC" +
                "Zg9kFgICAQ8QZA8WBmYCAQICAgMCBAIFFgYQBQMtLS0FAy0tLWcQBQMwMDEFAzAwMWcQBQMwMDIFAzAwMmcQBQMwMD" +
                "MFAzAwM2cQBQMxNzUFAzE3NWcQBQMyNTEFAzI1MWdkZGQBRJ/qa2v0OAMMeRdkpd2XFiCltKiJjyXS6doF/w0EQg==";
    }
}