# PlainRequest

Uma forma simples com ótimo desempenho para realizar uma Requisição (https/http) no Android. É uma abstração da biblioteca Volley, para deixar mais fácil sua implementação.

Utilizando generics, realiza o parse de uma entidade (ex: Customer) para JSON no envio de parâmetros ou, de JSON para uma entidade obtendo o retorno desserializado no onSuccess.

```java

	PlainRequest.builder()
	    .get(URL)
	    .requestCallback(new RequestCallback<String>() {
	        @Override
	        public void onSuccess(String response, int statusCode) {
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
compile 'com.github.giovanimoura:plainrequest:0.0.2'
```

### Recomendações

Esta lib não é adequado para grandes operações de download ou o streaming.



### Exemplo

#### Iniciar biblioteca

Você deve iniciar a PlainRequest, incluindo o código abaixo no método onCreate de sua classe que estende a Application. Esse código será executado uma única vez.

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
Incluir na sua Activity.

```java

	PlainRequest.builder()
	    .get("test/plainrequest/customers")
	    .requestCallback(new RequestCallback<List<Customer>>() {
	        @Override
	        public void onSuccess(List<Customer> customers, int statusCode) {
	        }

	        @Override
	        public void onError(VolleyError error, String msgError, int statusCode) {
	        }
	    })
	    .request();
```

### Licença

(The MIT License)

Copyright (c) 2017 Giovani Moura <giovani.moura84@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
'Software'), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.