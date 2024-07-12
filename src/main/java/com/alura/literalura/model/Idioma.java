package com.alura.literalura.model;

public enum Idioma {
   EN("en", "inglés"),
   FR("fr", "francés"),
   ES("es", "español"),
   DE("de", "alemán"),
   IT("it", "italiano"),
   PT("pt", "portugués"),
   RU("ru", "ruso"),
   ZH("zh", "chino"),
   JA("ja", "japonés"),
   AR("ar", "árabe"),
   KO("ko", "coreano"),
   HI("hi", "hindi"),
   BN("bn", "bengalí"),
   PA("pa", "panyabí"),
   VI("vi", "vietnamita"),
   TR("tr", "turco"),
   NL("nl", "neerlandés"),
   PL("pl", "polaco"),
   RO("ro", "rumano"),
   EL("el", "griego"),
   SV("sv", "sueco"),
   HU("hu", "húngaro"),
   CS("cs", "checo"),
   DA("da", "danés"),
   FI("fi", "finés"),
   NO("no", "noruego"),
   TH("th", "tailandés"),
   HE("he", "hebreo"),
   UR("ur", "urdu"),
   FA("fa", "persa"),
   UK("uk", "ucraniano"),
   MS("ms", "malayo"),
   ID("id", "indonesio"),
   TL("tl", "tagalo"),
   MY("my", "birmano"),
   TA("ta", "tamil"),
   MR("mr", "maratí"),
   KN("kn", "canarés"),
   GU("gu", "guyaratí"),
   TE("te", "telugú"),
   ML("ml", "malayalam"),
   AM("am", "amárico"),
   SW("sw", "suajili"),
   IU("iu", "inuktitut"),
   AF("af", "afrikáans");

   private final String codigo;
   private final String nombre;

   Idioma(String codigo, String nombre) {
      this.codigo = codigo;
      this.nombre = nombre;
   }

   public static Idioma fromCodigo(String text) {
      for (Idioma idioma : Idioma.values()) {
         if (idioma.codigo.equalsIgnoreCase(text)) {
            return idioma;
         }
      }
      throw new IllegalArgumentException("Ningún idioma encontrado con el código: " + text);
   }

   public static Idioma fromNombre(String text) {
      for (Idioma idioma : Idioma.values()) {
         if (idioma.nombre.equalsIgnoreCase(text)) {
            return idioma;
         }
      }
      throw new IllegalArgumentException("Ningún idioma encontrado con el nombre: " + text);
   }

   public String getCodigo() {
      return codigo;
   }

   public String getNombre() {
      return nombre;
   }
}
