# example-rest

Шаги по сборке и запуску:
<li>1. Собрать war с помощью maven (mvn package -DskipTests=true)</li>
<li>2. На PostgreSQL выполнить скрипт src/main/resources/init.sql</li>
<li>3. Настроить Hibernate (hibernate.cfg.xml)</li>
<li>4. Задеплоить war на сервер/серверы приложений (допускается одновременная работа нескольких экземпляров приложения)</li>
<li>5. Настроить параметры тестирования (testing.properties)</li>
<li>6. Запустить тесты</li>
