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
public class Huidig {

    public static final int cFaseNull = 0;
    public static final int cFaseDag = 1;
    public static final int cFaseSchemer = 2;
    public static final int cFaseAvond = 3;
    public static final int cFaseNacht = 4;

    private ZonedDateTime mZonsOndergang;
    private ZonedDateTime mStartLichtAan;
    private ZonedDateTime mLichtUit;
    private ZonedDateTime mBijwerken;
    private int mFase;
    private int mLichtMeting;

    public Huidig() {
        mZonsOndergang = ZonedDateTime.now();
        mStartLichtAan = mZonsOndergang;
        mLichtUit = mZonsOndergang;
        mBijwerken = mZonsOndergang;
        mFase = 0;
        mLichtMeting = 0;
    }

    public Huidig(String pZonsOndergang, String pStartLichtAan, String pLichtUit, String pBijwerken, int pFase, int pLichtMeting) {
        mZonsOndergang = ZonedDateTime.parse(pZonsOndergang);
        mStartLichtAan = ZonedDateTime.parse(pStartLichtAan);
        mLichtUit = ZonedDateTime.parse(pLichtUit);
        mBijwerken = ZonedDateTime.parse(pBijwerken);
        mFase = pFase;
        mLichtMeting = pLichtMeting;
    }

    public ZonedDateTime xZonsOndergang() {
        return mZonsOndergang;
    }

    public void xZonsOndergang(ZonedDateTime pZonsOndergang) {
        mZonsOndergang = pZonsOndergang;
    }

    public ZonedDateTime xStartLichtAan() {
        return mStartLichtAan;
    }

    public void xStartLichtAan(ZonedDateTime pStartLichtAan) {
        mStartLichtAan = pStartLichtAan;
    }

    public ZonedDateTime xLichtUit() {
        return mLichtUit;
    }

    public void xLichtUit(ZonedDateTime pLichtUit) {
        mLichtUit = pLichtUit;
    }

    public ZonedDateTime xBijwerken() {
        return mBijwerken;
    }

    public void xBijwerken(ZonedDateTime pBijwerken) {
        mBijwerken = pBijwerken;
    }

    public int xFase() {
        return mFase;
    }

    public void xFase(int pFase) {
        if (pFase >= cFaseNull) {
            if (pFase <= cFaseNacht) {
                mFase = pFase;
            }
        }
    }

    public int xLichtMeting() {
        return mLichtMeting;
    }

    public void xLichtMeting(int pLichtMeting) {
        mLichtMeting = pLichtMeting;
    }

    public void xZetStartLichtaan() {
//        mStartLichtAan = mZonsOndergang.minusHours(8);
        mStartLichtAan = mZonsOndergang.minusHours(1);
    }

    public void xStartLichtAanVerwerkt() {
        mStartLichtAan = mZonsOndergang.plusDays(1).minusHours(1);
    }

    public void xLichtUitVerwerkt() {
        mLichtUit = mLichtUit.plusDays(1);
    }

    public void xBijwerkenVerwerkt() {
        mBijwerken = mBijwerken.plusDays(1);
    }

}
