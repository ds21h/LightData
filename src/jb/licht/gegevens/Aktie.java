/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jb.licht.gegevens;

import java.time.ZonedDateTime;

/**
 *
 * @author Jan
 */
public class Aktie {

    public static final String cAktieRefresh = "Refresh";
    public static final String cAktieZetLichtUit = "ZetLichtUit";
    public static final String cAktieZetAan = "ZetAan";
    public static final String cAktieZetUit = "ZetUit";
    public static final String cAktieZetAllesAan = "ZetAllesAan";
    public static final String cAktieZetAllesUit = "ZetAllesUit";
    public static final String cAktieGeen = "Geen";

    private final int mID;
    private ZonedDateTime mGemaakt; 
    private ZonedDateTime mUitvoeren;
    private final String mType;
    private final String mPar;
    private final boolean mKlaar;

    public Aktie(String pType) {
        mID = -1;
        mGemaakt = ZonedDateTime.now();
        mUitvoeren = null;
        mType = pType;
        mPar = "";
        mKlaar = false;
    }

    public Aktie(String pType, String pPar) {
        mID = -1;
        mGemaakt = ZonedDateTime.now();
        mUitvoeren = null;
        mType = pType;
        mPar = pPar;
        mKlaar = false;
    }

    public Aktie(ZonedDateTime pMoment, String pType, String pPar) {
        mID = -1;
        mGemaakt = ZonedDateTime.now();
        mUitvoeren = pMoment;
        mType = pType;
        mPar = pPar;
        mKlaar = false;
    }

    public Aktie(int pID, String pGemaakt, String pUitvoeren, String pType, String pPar, boolean pKlaar) {
        mID = pID;
        if (pGemaakt == null){
            mGemaakt = null;
        } else {
            mGemaakt = ZonedDateTime.parse(pGemaakt);
        }
        if (pUitvoeren == null){
            mUitvoeren = null;
        } else {
            mUitvoeren = ZonedDateTime.parse(pUitvoeren);
        }
        mType = pType;
        mPar = pPar;
        mKlaar = pKlaar;
    }

    public int xID(){
        return mID;
    }
    
    public ZonedDateTime xGemaakt(){
        return mGemaakt;
    }
    
    public ZonedDateTime xUitvoeren(){
        return mUitvoeren;
    }
    
    public String xType() {
        return mType;
    }

    public String xPar() {
        return mPar;
    }
    
    public boolean xKlaar(){
        return mKlaar;
    }
}
