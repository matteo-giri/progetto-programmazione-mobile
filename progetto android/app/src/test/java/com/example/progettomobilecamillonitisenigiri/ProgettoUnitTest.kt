package com.example.progettomobilecamillonitisenigiri

import android.util.Log
import com.example.progettomobilecamillonitisenigiri.Model.Corso
import com.example.progettomobilecamillonitisenigiri.Model.Lezione
import com.example.progettomobilecamillonitisenigiri.ViewModels.CorsoUtils
import com.example.progettomobilecamillonitisenigiri.ViewModels.FirebaseConnection
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock



@RunWith(JUnit4::class)
class ProgettoUnitTest {


    lateinit var firebaseConnection: FirebaseConnection
    var corsoUtils = CorsoUtils()
    val lista = ArrayList<Corso>()
    val lezioniCorso1 = ArrayList<Lezione>()

    @Before
    fun setUp() {

        lezioniCorso1.add(Lezione("Lezione0","Impariamo Java0","","0"))
        lezioniCorso1.add(Lezione("Lezione1","Impariamo Java1","","1"))
        lezioniCorso1.add( Lezione("Lezione2","Impariamo Java1","","2"))

        lista.add(Corso("Java per principianti","Impara le basi del java","Adriano Mancini","0","https://corsi-informatica.com/wp-content/upload...","10€","Informatica", HashMap(), ArrayList(),ArrayList(), ArrayList()
        ))
        lista.add(Corso("Cuciniamo insieme a Tiso","Impara a cucinare","","1","https://www.aiafood.com/sites/default/files/sty...","10€","robotica", HashMap(), lezioniCorso1,ArrayList(),ArrayList()))
        lista.add(Corso("Robotics course","Robots are cool! We can't imagine the world today can run without the help of robots. Robots are every where - right from cleaning homes, cooking food, to assembling cars in huge manufacturing plants and defence. Robots have found a place in almost every possible ","AndrewNG","3","https://i0.wp.com/www.alzarating.com/wp-content/uploads/2019/07/alzarating-58.jpg?fit=800%2C351&ssl=1","10€","cucina", HashMap(), ArrayList(),ArrayList(),ArrayList()))
        lista.add(Corso("Corso base di design","Il corso intende formare un professionista che ...","Mario Rossi","4","https://lifelearning.it/wp-content/uploads/2015/03/interior-design.png","10€","design", HashMap(), ArrayList(),ArrayList(),ArrayList()))
        lista.add(Corso("Corso di marketing per principianti","Corso base sul marketing. In questo corso imparerai a diventare un vero genio del marketing!","","5","https://www.mirkocuneo.it/wp-content/uploads/2019/12/web-marketing-per-imprenditori-1024x632.jpg","","marketing",HashMap(), ArrayList(),ArrayList(),ArrayList()))
        var iscrizioni = ArrayList<String>()
        with(iscrizioni){
            add("1")
            add("19")
            add("0")
        }
        var catPref = ArrayList<String>()
        with(catPref){
            add("Informatica")
            add("design")
        }
        var wishlist = ArrayList<String>()
        with(wishlist){
            add("5")
            add("4")
            add("3")
        }
      //  user = User("Lorenzo","Tiseni",iscrizioni,catPref,wishlist,ArrayList(), UltimaLezione())

        //firebaseConnection = mock(FirebaseConnection::class.java)


        corsoUtils.setCorsi(lista)
        //Log.d("ciao", firebaseConnection.getListaCorsi().toString())
    }

    @Test
    fun corsi_isOfCorrectClass() {
        assertTrue(corsoUtils.getCorsi() is ArrayList<Corso>)
    }
    @Test
    fun corsiPerCat_isOfCorrectClass() {
        assertTrue(corsoUtils.getCorsiPerCat() is HashMap<String,ArrayList<Corso>>)
    }

    @Test
    fun listaRecenti_isOfCorrectClass() {
        assertTrue(firebaseConnection.getListaPopolari(lista) is ArrayList<Corso>)
    }

    @Test
    fun listaRecenti_isNotNull() {
        assertNotNull(firebaseConnection.getListaPopolari(lista))
    }

    @Test
    fun corso_isOfCorrectClass() {
        val corso =corsoUtils.getCorsi().get(0)
        assertTrue(corso is Corso)

    }

    @Test
    fun corsoSpecifico_hasCorrectValues() {
        val corso = corsoUtils.getCorsi().get(3)
        assertEquals("Corso base di design", corso?.titolo)
        assertEquals("10€", corso?.prezzo)
        assertEquals("Mario Rossi", corso?.docente)
        assertNotNull(corso?.lezioni)
    }


    @Test
    fun lezioni_isOfCorrectClass() {
        assertTrue(corsoUtils.getLezioni() is HashMap<String, ArrayList<Lezione>>)
    }

    @Test
    fun lezioni_isEmptyIfNoDbConnection() {
        assertNotNull(corsoUtils.getLezioni().isEmpty())
    }

    @Test
    fun lezione_isNullIfNoDbConnection() {
        val lezione = corsoUtils.getLezioni().get(0)?.get(0)
        assertNull(lezione)

    }

    @Test
    fun lezioneSpecifica_hasCorrectValues() {
        val corso1 = corsoUtils.getCorsi().get(1)
        val lezione1 = corso1.lezioni.get(1)
        assertEquals("Lezione1", lezione1?.titolo)
        assertEquals("1", lezione1?.id)
        assertEquals("Impariamo Java1", lezione1?.descrizione)
    }


}



