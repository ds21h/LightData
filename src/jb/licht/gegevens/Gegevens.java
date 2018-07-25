/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jb.licht.gegevens;

import jb.licht.klassen.Instelling;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import jb.licht.klassen.Schakelaar;

/**
 *
 * @author Jan
 */
public class Gegevens {

    private int mStatus;
    private String mMelding = "";
    public static final int cNotSet = -1;
    public static final int cOK = 0;
    public static final int cSQLite_not_found = 100;
    public static final int cSQL_error = 200;

    private Connection mConn;

    public Gegevens() {
        mStatus = cNotSet;

        try {
            Class.forName("org.sqlite.JDBC");
            mConn = DriverManager.getConnection("jdbc:sqlite:/usr/local/licht/gegevens/licht.db");
//            mConn = DriverManager.getConnection("jdbc:sqlite:d:/source/netbeans/lichtschakel/licht.db");
            mStatus = cOK;
        } catch (ClassNotFoundException ex) {
            mStatus = cSQLite_not_found;
            xSchrijfLog("SQLite niet gevonden");
        } catch (SQLException ex) {
            xSchrijfLog("SQLException!");
            mStatus = cSQL_error;
            mMelding = ex.getMessage();
            xSchrijfLog("Connection mislukt: " + mMelding);
        }
    }

    public int xStatus() {
        return mStatus;
    }

    public Instelling xInstelling() {
        double lLengte;
        double lBreedte;
        int lLichtUitUur;
        int lLichtUitMin;
        int lUitTijd;
        int lSensorGrens;
        int lSensorDrempel;
        int lMaxSensor;
        int lPeriodeDonker;
        int lPeriodeMinuut;
        int lPeriodeSec;
        Statement lStm;
        ResultSet lRes;
        Instelling lInst;
        String lSql = "SELECT Lengte, Breedte, LichtUitUur, LichtUitMin, UitTijd, SensorGrens, SensorDrempel, MaxSensor, PeriodeDonker, PeriodeMinuut, PeriodeSec "
                + "FROM Instelling "
                + "WHERE ID = 'Licht';";

        mMelding = "";
        lInst = new Instelling();
        if (mStatus == cOK) {
            try {
                lStm = mConn.createStatement();
                lRes = lStm.executeQuery(lSql);
                if (lRes.next()) {
                    lLengte = lRes.getDouble("Lengte");
                    lBreedte = lRes.getDouble("Breedte");
                    lLichtUitUur = lRes.getInt("LichtUitUur");
                    lLichtUitMin = lRes.getInt("LichtUitMin");
                    lUitTijd = lRes.getInt("UitTijd");
                    lSensorGrens = lRes.getInt("SensorGrens");
                    lSensorDrempel = lRes.getInt("SensorDrempel");
                    lMaxSensor = lRes.getInt("MaxSensor");
                    lPeriodeDonker = lRes.getInt("PeriodeDonker");
                    lPeriodeMinuut = lRes.getInt("PeriodeMinuut");
                    lPeriodeSec = lRes.getInt("PeriodeSec");
                    lInst = new Instelling(lLengte, lBreedte, lLichtUitUur, lLichtUitMin, lUitTijd, lSensorGrens, lSensorDrempel, lMaxSensor, lPeriodeDonker, lPeriodeMinuut, lPeriodeSec);
                }
                lRes.close();
                lStm.close();
            } catch (SQLException ex) {
                mStatus = cSQL_error;
                mMelding = ex.getMessage();
                xSchrijfLog("xInstelling: SQL error " + mMelding);
            }
        }
        return lInst;
    }

    public void xWijzigInstelling(Instelling pInstelling) {
        Statement lStm;
        String lSql;

        if (mStatus == cOK) {
            lSql = "UPDATE Instelling "
                    + "SET "
                    + "Lengte = '" + String.valueOf(pInstelling.xLengte()) + "', "
                    + "Breedte = '" + String.valueOf(pInstelling.xBreedte()) + "', "
                    + "LichtUitUur = '" + String.valueOf(pInstelling.xLichtUitUur()) + "', "
                    + "LichtUitMin = '" + String.valueOf(pInstelling.xLichtUitMin()) + "', "
                    + "UitTijd = '" + String.valueOf(pInstelling.xUitTijd()) + "', "
                    + "SensorGrens = '" + String.valueOf(pInstelling.xSensorGrens()) + "', "
                    + "SensorDrempel = '" + String.valueOf(pInstelling.xSensorDrempel()) + "', "
                    + "MaxSensor = '" + String.valueOf(pInstelling.xMaxSensor()) + "', "
                    + "PeriodeDonker = '" + String.valueOf(pInstelling.xPeriodeDonker()) + "', "
                    + "PeriodeMinuut = '" + String.valueOf(pInstelling.xPeriodeMinuut()) + "', "
                    + "PeriodeSec = '" + String.valueOf(pInstelling.xPeriodeSec()) + "' "
                + "WHERE ID = 'Licht';";
            try {
                lStm = mConn.createStatement();
                lStm.executeUpdate(lSql);
                lStm.close();
            } catch (SQLException ex) {
                mStatus = cSQL_error;
                mMelding = ex.getMessage();
                xSchrijfLog("xWijzigInstelling: SQL error " + mMelding);
            }
        }
    }

    public List<Schakelaar> xSchakelaars() {
        List<Schakelaar> lSchakelaars;
        Schakelaar lSchakelaar;
        int lVolgNummer;
        String lNaam;
        boolean lAktief;
        String lType;
        String lGroep;
        String lPunt;
        int lPauze;
        String lIP;
        Statement lStm;
        ResultSet lRes;
        String lSql = "SELECT VolgNummer, Naam, Aktief, Type, Groep, Punt, Pauze, IP "
                + "FROM Schakelaar "
                + "ORDER BY VolgNummer, Naam;";

        lSchakelaars = new ArrayList<>();
        mMelding = "";
        if (mStatus == cOK) {
            try {
                lStm = mConn.createStatement();
                lRes = lStm.executeQuery(lSql);
                while (lRes.next()) {
                    lVolgNummer = lRes.getInt("VolgNummer");
                    lNaam = lRes.getString("Naam");
                    lAktief = lRes.getBoolean("Aktief");
                    lType = lRes.getString("Type");
                    lGroep = lRes.getString("Groep");
                    if (lGroep == null){
                        lGroep = "";
                    }
                    lPunt = lRes.getString("Punt");
                    if (lPunt == null){
                        lPunt = "";
                    }
                    lPauze = lRes.getInt("Pauze");
                    lIP = lRes.getString("IP");
                    if (lIP == null){
                        lIP = "";
                    }
                    lSchakelaar = new Schakelaar(lVolgNummer, lNaam, lAktief, lType, lGroep, lPunt, lPauze, lIP);
                    lSchakelaars.add(lSchakelaar);
                }
                lRes.close();
                lStm.close();
            } catch (SQLException ex) {
                mStatus = cSQL_error;
                mMelding = ex.getMessage();
                xSchrijfLog("xSchakelaars: SQL error " + mMelding);
            }
        }
        return lSchakelaars;
    }

    public Schakelaar xSchakelaar(String pNaam) {
        Schakelaar lSchakelaar;
        int lVolgNummer;
        String lNaam;
        boolean lAktief;
        String lType;
        String lGroep;
        String lPunt;
        int lPauze;
        String lIP;
        Statement lStm;
        ResultSet lRes;
        String lSql = "SELECT VolgNummer, Naam, Aktief, Type, Groep, Punt, Pauze, IP "
                + "FROM Schakelaar "
                + "WHERE Naam = '" + pNaam + "';";

        mMelding = "";
        lSchakelaar = new Schakelaar();
        if (mStatus == cOK) {
            try {
                lStm = mConn.createStatement();
                lRes = lStm.executeQuery(lSql);
                if (lRes.next()) {
                    lVolgNummer = lRes.getInt("VolgNummer");
                    lNaam = lRes.getString("Naam");
                    lAktief = lRes.getBoolean("Aktief");
                    lType = lRes.getString("Type");
                    lGroep = lRes.getString("Groep");
                    if (lGroep == null){
                        lGroep = "";
                    }
                    lPunt = lRes.getString("Punt");
                    if (lPunt == null){
                        lPunt = "";
                    }
                    lPauze = lRes.getInt("Pauze");
                    lIP = lRes.getString("IP");
                    if (lIP == null){
                        lIP = "";
                    }
                    lSchakelaar = new Schakelaar(lVolgNummer, lNaam, lAktief, lType, lGroep, lPunt, lPauze, lIP);
                }
                lRes.close();
                lStm.close();
            } catch (SQLException ex) {
                mStatus = cSQL_error;
                mMelding = ex.getMessage();
                xSchrijfLog("xSchakelaar: SQL error " + mMelding);
            }
        }
        return lSchakelaar;
    }

    public void xWijzigSchakelaar(Schakelaar pSchakelaar) {
        Statement lStm;
        String lSql;
        int lAktief;
        String lGroep;
        String lPunt;
        String lIP;

        if (pSchakelaar.xGroep().equals("")){
            lGroep = "null";
        } else {
            lGroep = "'" + pSchakelaar.xGroep() + "'";
        }
        if (pSchakelaar.xPunt().equals("")){
            lPunt = "null";
        } else {
            lPunt = "'" + pSchakelaar.xPunt() + "'";
        }
        if (pSchakelaar.xIP().equals("")){
            lIP = "null";
        } else {
            lIP = "'" + pSchakelaar.xIP() + "'";
        }
        if (pSchakelaar.xAktief()) {
            lAktief = 1;
        } else {
            lAktief = 0;
        }
        if (mStatus == cOK) {
            lSql = "UPDATE Schakelaar "
                    + "SET "
                    + "VolgNummer = '" + pSchakelaar.xVolgNummer() + "', "
                    + "Aktief = '" + lAktief + "', "
                    + "Type = '" + pSchakelaar.xType() + "', "
                    + "Groep = " + lGroep + ", "
                    + "Punt = " + lPunt + ", "
                    + "Pauze = '" + pSchakelaar.xPauze() + "', "
                    + "IP = " + lIP + " "
                    + "WHERE Naam = '" + pSchakelaar.xNaam() + "';";
            try {
                lStm = mConn.createStatement();
                lStm.executeUpdate(lSql);
                lStm.close();
            } catch (SQLException ex) {
                mStatus = cSQL_error;
                mMelding = ex.getMessage();
                xSchrijfLog("xWijzigSchakelaar: SQL error " + mMelding);
            }
        }
    }

    public void xVerwijderSchakelaar(String pSchakelaarId) {
        Statement lStm;
        String lSql;

        if (mStatus == cOK) {
            lSql = "DELETE from Schakelaar "
                    + "WHERE Naam = '" + pSchakelaarId + "';";
            try {
                lStm = mConn.createStatement();
                lStm.executeUpdate(lSql);
                lStm.close();
            } catch (SQLException ex) {
                mStatus = cSQL_error;
                mMelding = ex.getMessage();
                xSchrijfLog("xVerwijderSchakelaar: SQL error " + mMelding);
            }
        }
    }

    public void xNieuweSchakelaar(Schakelaar pSchakelaar) {
        Statement lStm;
        String lSql;
        int lAktief;
        String lGroep;
        String lPunt;
        String lIP;

        if (pSchakelaar.xGroep().equals("")){
            lGroep = "null";
        } else {
            lGroep = "'" + pSchakelaar.xGroep() + "'";
        }
        if (pSchakelaar.xPunt().equals("")){
            lPunt = "null";
        } else {
            lPunt = "'" + pSchakelaar.xPunt() + "'";
        }
        if (pSchakelaar.xIP().equals("")){
            lIP = "null";
        } else {
            lIP = "'" + pSchakelaar.xIP() + "'";
        }

        if (pSchakelaar.xAktief()) {
            lAktief = 1;
        } else {
            lAktief = 0;
        }
        if (mStatus == cOK) {
            lSql = "INSERT INTO Schakelaar (VolgNummer, Naam, Aktief, Type, Groep, Punt, Pauze, IP) "
                    + "VALUES ('" + pSchakelaar.xVolgNummer() + "', "
                    + "'" + pSchakelaar.xNaam() + "', "
                    + "'" + lAktief  + "', "
                    + "'" + pSchakelaar.xType()  + "', "
                    + lGroep + ", "
                    + lPunt + ", " 
                    + "'" + pSchakelaar.xPauze()  + "', "
                    + lIP + ");";
            try {
                lStm = mConn.createStatement();
                lStm.executeUpdate(lSql);
                lStm.close();
            } catch (SQLException ex) {
                mStatus = cSQL_error;
                mMelding = ex.getMessage();
                xSchrijfLog("xNieuweSchakelaar: SQL error " + mMelding);
            }
        }
    }

    public Huidig xHuidig() {
        String lZonsOndergang;
        String lStartLichtAan;
        String lLichtUit;
        String lBijwerken;
        int lFase;
        int lLichtMeting;
        Statement lStm;
        ResultSet lRes;
        Huidig lHuidig;
        String lSql = "SELECT ZonsOndergang, StartLichtAan, LichtUit, Bijwerken, Fase, LichtMeting "
                + "FROM Huidig "
                + "WHERE ID = 'Licht';";

        mMelding = "";
        lHuidig = new Huidig();
        if (mStatus == cOK) {
            try {
                lStm = mConn.createStatement();
                lRes = lStm.executeQuery(lSql);
                if (lRes.next()) {
                    lZonsOndergang = lRes.getString("ZonsOndergang");
                    lStartLichtAan = lRes.getString("StartLichtAan");
                    lLichtUit = lRes.getString("LichtUit");
                    lBijwerken = lRes.getString("Bijwerken");
                    lFase = lRes.getInt("Fase");
                    lLichtMeting = lRes.getInt("LichtMeting");
                    lHuidig = new Huidig(lZonsOndergang, lStartLichtAan, lLichtUit, lBijwerken, lFase, lLichtMeting);
                }
                lRes.close();
                lStm.close();
            } catch (SQLException ex) {
                mStatus = cSQL_error;
                mMelding = ex.getMessage();
                xSchrijfLog("xHuidig: SQL error " + mMelding);
            }
        }
        return lHuidig;
    }

    public void xHuidig(Huidig pHuidig) {
        Statement lStm;
        String lSql;
        DateTimeFormatter lFormat;

        lFormat = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        if (mStatus == cOK) {
            lSql = "UPDATE Huidig "
                    + "SET "
                    + "ZonsOndergang = '" + pHuidig.xZonsOndergang().format(lFormat) + "', "
                    + "StartLichtAan = '" + pHuidig.xStartLichtAan().format(lFormat) + "', "
                    + "LichtUit = '" + pHuidig.xLichtUit().format(lFormat) + "', "
                    + "Bijwerken = '" + pHuidig.xBijwerken().format(lFormat) + "', "
                    + "Fase = '" + pHuidig.xFase() + "', "
                    + "LichtMeting = '" + pHuidig.xLichtMeting() + "' "
                    + "WHERE ID = 'Licht';";
            try {
                lStm = mConn.createStatement();
                lStm.executeUpdate(lSql);
                lStm.close();
            } catch (SQLException ex) {
                mStatus = cSQL_error;
                mMelding = ex.getMessage();
                xSchrijfLog("xHuidig_Update: SQL error " + mMelding);
            }
        }
    }

    public void xNieuweAktie(Aktie pAktie) {
        Statement lStm;
        String lSql;
        DateTimeFormatter lFormat;
        String lGemaakt;
        String lUitvoeren;

        if (mStatus == cOK) {
        lFormat = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        if (pAktie.xGemaakt() == null){
            lGemaakt = "";
        } else {
            lGemaakt = pAktie.xGemaakt().format(lFormat);
        }
        if (pAktie.xUitvoeren() == null){
            lUitvoeren = "";
        } else {
            lUitvoeren = pAktie.xUitvoeren().format(lFormat);
        }

            lSql = "INSERT INTO Aktie (Gemaakt, Uitvoeren, Type, Par, Klaar) "
                    + "VALUES ('" + lGemaakt + "', '" + lUitvoeren + "', '" + pAktie.xType() + "', '" + pAktie.xPar() + "', 0);";
            try {
                lStm = mConn.createStatement();
                lStm.executeUpdate(lSql);
                lStm.close();
            } catch (SQLException ex) {
                mStatus = cSQL_error;
                mMelding = ex.getMessage();
                xSchrijfLog("xNieuweAktie: SQL error " + mMelding);
            }
        }
    }

    public List<Aktie> xAkties() {
        List<Aktie> lAkties;
        Aktie lAktie;
        int lID;
        String lType;
        String lGemaakt;
        String lUitvoeren;
        String lPar;
        boolean lKlaar;
        Statement lStm;
        ResultSet lRes;
        String lSql = "SELECT ID, Gemaakt, Uitvoeren, Type, Par, Klaar "
                + "FROM Aktie "
                + "WHERE Klaar = 0 "
                + "Order by Uitvoeren, Gemaakt;";

        lAkties = new ArrayList<>();
        mMelding = "";
        if (mStatus == cOK) {
            try {
                lStm = mConn.createStatement();
                lRes = lStm.executeQuery(lSql);
                while (lRes.next()) {
                    lID = lRes.getInt("ID");
                    lGemaakt = lRes.getString("Gemaakt");
                    if (lGemaakt != null && lGemaakt.equals("")){
                        lGemaakt = null;
                    }
                    lUitvoeren = lRes.getString("Uitvoeren");
                    if (lUitvoeren != null && lUitvoeren.equals("")){
                        lUitvoeren = null;
                    }
                    lType = lRes.getString("Type");
                    lPar = lRes.getString("Par");
                    lKlaar = lRes.getBoolean("Klaar");
                    lAktie = new Aktie(lID, lGemaakt, lUitvoeren, lType, lPar, lKlaar);
                    lAkties.add(lAktie);
                }
                lRes.close();
                lStm.close();
            } catch (SQLException ex) {
                mStatus = cSQL_error;
                mMelding = ex.getMessage();
                xSchrijfLog("xAkties: SQL error " + mMelding);
            }
        }

        return lAkties;
    }

    public void xAktieUitgevoerd(Aktie pAktie) {
        Statement lStm;
        String lSql;
        if (mStatus == cOK) {
            lSql = "UPDATE Aktie "
                    + "SET "
                    + "Klaar = 1 "
                    + "WHERE ID = " + pAktie.xID() + ";";
            try {
                lStm = mConn.createStatement();
                lStm.executeUpdate(lSql);
                lStm.close();
            } catch (SQLException ex) {
                mStatus = cSQL_error;
                mMelding = ex.getMessage();
                xSchrijfLog("xAktieUitgevoerd: SQL error " + mMelding);
            }
        }
    }

    public void xAktieSchakelaarUitgevoerd(Schakelaar pSchakelaar) {
        Statement lStm;
        String lSql;
        if (mStatus == cOK) {
            lSql = "UPDATE Aktie "
                    + "SET "
                    + "Klaar = 1 "
                    + "WHERE Par = '" + pSchakelaar.xNaam()+ "';";
            try {
                lStm = mConn.createStatement();
                lStm.executeUpdate(lSql);
                lStm.close();
            } catch (SQLException ex) {
                mStatus = cSQL_error;
                mMelding = ex.getMessage();
                xSchrijfLog("xAktieUitgevoerd: SQL error " + mMelding);
            }
        }
    }

    public final void xSchrijfLog(String pMelding) {
        LocalDateTime lNu;
        Statement lStm;
        String lSql;

        lNu = LocalDateTime.now();
        if (mStatus == cOK) {
            lSql = "INSERT INTO Log (Tijdstip, Inhoud) "
                    + "VALUES ('" + lNu.toString() + "', '" + pMelding + "');";
            try {
                lStm = mConn.createStatement();
                lStm.executeUpdate(lSql);
                lStm.close();
            } catch (SQLException ex) {
                mStatus = cSQL_error;
                mMelding = ex.getMessage();
                System.out.println(lNu.toString() + " " + mMelding);
            }
        } else {
            System.out.println(lNu.toString() + " Status niet OK: " + mStatus);
        }
        System.out.println(lNu.toString() + " " + pMelding);
    }

    public void xAfsluit() {
        try {
            mConn.close();
        } catch (SQLException ex) {
            mMelding = ex.getMessage();
        }
    }
}
