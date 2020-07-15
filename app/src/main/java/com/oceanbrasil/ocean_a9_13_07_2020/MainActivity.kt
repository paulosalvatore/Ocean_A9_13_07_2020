package com.oceanbrasil.ocean_a9_13_07_2020

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.lang.Exception
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // O que eu escrever aqui será processado na MainThread (ou UI Thread)

        // Worker Thread

        // Com a comunicação da ida, cria uma thread separada
        // Também preciso me preocupar com a volta, que é quando eu atualizo informações
        // na tela

        // PARALELO
        // CONCORRENTE ou SEQUENCIAL

        btWorker.setOnClickListener {
            iniciarWorkerThread()
        }

        btAsyncKotlin.setOnClickListener {
//            Thread.sleep(20000)
//            iniciarAsyncKotlin()
        }
    }

    private fun iniciarAsyncKotlin() {
        // Exemplo de execução na Main Thread com Kotlin
        runOnUiThread {

        }

        // Exemplo de Async e de UiThread com a biblioteca Anko
        doAsync {
            // Worker Thread

            val imagem = carregarImagemUrl("http...")

            uiThread {
                // Ui Thread

                imageView.setImageBitmap(imagem)
            }
        }
    }

    private fun iniciarWorkerThread() {
        // Estamos na Main Thread
        // Consigo acessar elementos de UI

        tvPrincipal.text = "Paulo"

//        imageView.post {
//        imageView.setImageResource(R.drawable.ic_launcher_foreground)
//        }

        Thread(Runnable {
            // Estamos em uma nova Worker Thread

//            Thread.sleep(1000)

            tvPrincipal.post {
                tvPrincipal.text = "Texto no inicio da worker"
            }

//            val url = "https://www.acritica.com/uploads/news/image/767389/show_show_OCEAN_3B680708-4F30-4C43-BB1D-3C0FF32B32D1.jpg"
            val url = "https://img.global.news.samsung.com/br/wp-content/uploads/2016/08/Samsung-14.jpg"
            val imagemCarregada = carregarImagemUrl(url)
            Log.d("CARREGAR_IMAGEM", imagemCarregada.toString())

            // Ainda estamos na WorkerThread

            imageView.post {
                // Execução do ImageView será feita na MainThread
                imageView.setImageBitmap(imagemCarregada)
            }

//            tvPrincipal.text = "Salvatore"

//            imageView.setImageResource(R.drawable.ic_launcher_foreground)

            // Não consigo acessar elementos de UI,
            // apenas com alguns facilitadores
        }).start()

        tvPrincipal.text = "Texto no final do método 'iniciarWorkerThread'."
    }

    private fun carregarImagemUrl(imagemUrl: String) = try {
        val url = URL(imagemUrl)
        val conteudoUrl = url.openConnection().getInputStream()

        BitmapFactory.decodeStream(conteudoUrl)
    } catch (e: Exception) {
        Log.e("CARREGAR_IMAGEM", "Erro ao carregar imagem a partir da URL.", e)

        null
    }
}