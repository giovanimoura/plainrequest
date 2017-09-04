# PlainRequest

Uma forma simples e com ótimo desempenho para realizar uma Requisição (https/http) no Android. É uma abstração da biblioteca Volley, para deixar mais fácil sua implementação.

Utilizando generics, realiza o parse de uma entidade (ex: Customer) para JSON no envio de parâmetros ou, de JSON para uma entidade obtendo o retorno desserializado no onSuccess.

```java
PlainRequest.builder()
    .get(URL)
    .result(new ResultRequest<String>() {
        @Override
        public void onSuccess(String response, int statusCode) {
        }

        @Override
        public void onError(VolleyError error, String msgError, int statusCode) {
        }
    })
```

```java
PlainRequest.builder()
    .get(URL)
    .result(new ResultRequest<Customer>() {
        @Override
        public void onSuccess(Customer customer, int statusCode) {
        }

        @Override
        public void onError(VolleyError error, String msgError, int statusCode) {
        }
    })
```

### Pré Requisitos

* Android 4.0 ou superior

### Download

```
compile 'com.github.giovanimoura:plainrequest:1.0.0'
```

### Recomendações

Esta lib não é adequado para grandes operações de download ou o streaming



### Exemplo

#### Iniciar biblioteca

Você deve incluir o código abaixo no método onCreate de sua classe que estende a Application para iniciar a lib. Esse código será executado uma única vez

```java
PlainRequestQueue.builder()
    .urlDefault("https://private-fbf8ad-testplainrequest.apiary-mock.com/")
    .start(this);
```

#### Requisição do tipo GET

##### Entidade
```java
public class Customer {

    private int id;
    private String name;
}
```

##### Request
Incluir na sua Activity

```java
PlainRequest.builder()
    .get("test/plainrequest/customers")
    .result(new ResultRequest<List<Customer>>() {
        @Override
        public void onSuccess(List<Customer> customers, int statusCode) {
        }

        @Override
        public void onError(VolleyError error, String msgError, int statusCode) {
        }
    })
    .request();
```

### Documentação

Aborda as funcionalidades da bibliotéca

##### - PlainRequestQueue
Classe de declaração obrigatória para iniciar a bibliotéca

- **start(Application app)** - Inicializa a biblioteca PlainRequest (**declaração obrigatório**)

- **urlDefault(String urlDefault)** - URL utilizada em todas as requições concatenado com a URL informada no get/post/delete/put

- **header(String key, String value)** - Adiciona parâmetros no header da requisição

- **timeOutSeconds(int timeOut)** - Tempo limite de espera da requisição, em segundos (default 10 segundos)

- **release(boolean buildRelease)** - Define se o build da app é do tipo release para remover os log's em produção (default false)

- **tagRequest(String tagName)** - Adiciona uma identificação para a request

- **tlsVersion(String protocol)** - Define o protocolo SSL (default TLSv1.2)

- **sslSecurity(boolean enableSSL)** - Ativar/Desativar o SSL (default true)

##### - PlainRequest
Classe que executa da resquest dos tipos GET, POST, DELETE ou PUT

- **get(String url)** - Define uma request do tipo GET. URL será concatenada com urlDefault

- **post(String url)** - Define uma request do tipo POST. URL será concatenada com urlDefault

- **delete(String url)** - Define uma request do tipo DELETE. URL será concatenada com urlDefault

- **put(String url)** - Define uma request do tipo PUT. URL será concatenada com urlDefault

- **param(String key, String value)** - Adiciona parâmetros na request do tipo GET

- **param(Object param)** - Adiciona parâmetros na request do tipo POST/DELETE/PUT

- **param(String key, Object value)** - Adiciona parâmetros na request do tipo POST/DELETE/PUT

- **request()** - Executa a request (**declaração obrigatório**)

- **result(ResultRequest<T> resultRequest)** - Resultado da request de sucesso  (retorno genérico) ou erro. É possível definir um objeto/entidade como retorno, pois realizamos um parse do JSON conforme seu objeto

- **result(OnResultRequest<T> onResultRequest)** - Resultado da request de sucesso  (retorno genérico) ou erro utilizando uma interface. Isso permite que você possa declarar a interface na Activity, ou algo semelhante. É possível definir um objeto/entidade como retorno, pois realizamos um parse do JSON conforme seu objeto

    ##### - ResultRequest<T> / OnResultRequest<T>
	Classe de eventos do retorno da request

	- **onPreExecute()** - Executado antes da request, permitindo realizar processos como apresentar um ProgressDialog
	
	- **onSuccess(T response, int statusCode)** - Possui um retorno genérico que você deve definir ao instanciar a classe ResultRequest<T>/OnResultRequest<T>
	
		```
		Exemplo:
		
		.result(new ResultRequest<String>(){...}) - Obtem no onSuccess um retorno do tipo String
		
		.result(new ResultRequest<Customer>(){...}) - Obtem no onSuccess um retorno do tipo Customer populado de acordo com o JSON
		
		.result(new ResultRequest<List<Customer>>(){...}) - Obtem no onSuccess um retorno do tipo List<Customer> populado de acordo com o JSON
		```

	- **onError(VolleyError error, String msgError, int statusCode)** - Em caso de falha da request retorna as informações do erro

- **cancel(String tagName)** - Cancela a request de acordo com a tagName

- **clearUrlDefault()** - Limpa o valor da urlDefault, para informar a URL diretamente no get/post/delete/put

- **findJson(String nameFindJson)** - Define um campo para ser localizado e desserializado no JSON. É aplicado no response

- ***header(String key, String value)** - Adiciona parâmetros no header da requisição

- ***timeOutSeconds(int timeOut)** - Tempo limite de espera da requisição, em segundos (default 10 segundos)

- ***tagRequest(String tagName)** - Adiciona uma identificação para a request

- ***sslSecurity(boolean enableSSL)** - Ativar/Desativar o SSL (default true)

```
*Essas funcionalidades sobrescrevem as declarações de PlainRequestQueue, mas somente na request em que foram declaradas, sendo reinicializadas na próxima request
```

### Contribution

https://github.com/ulymarins

### License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details


