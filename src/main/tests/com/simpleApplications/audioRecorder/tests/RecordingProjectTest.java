package com.simpleApplications.audioRecorder.tests;

import com.simpleApplications.audioRecorder.daos.interfaces.IRecordingProjectDao;
import com.simpleApplications.audioRecorder.model.RecordingProject;
import com.simpleApplications.audioRecorder.verticles.HttpVertical;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * @author Nico Moehring
 */
@RunWith(VertxUnitRunner.class)
public class RecordingProjectTest extends AbstractGuiceAwareTest implements JsonSender {

    protected Vertx vertx;

    protected IRecordingProjectDao dao;

    protected HttpClient httpClient;

    protected String host = "127.0.0.1";

    protected String serverPath = "/recordingProjects";

    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.dao = this.injector.getInstance(IRecordingProjectDao.class);
        this.vertx = Vertx.vertx();
        this.httpClient = this.vertx.createHttpClient();

        this.vertx.deployVerticle(this.injector.getInstance(HttpVertical.class), new DeploymentOptions().setWorker(true));
    }

    @Test
    public void testGetEmptyJsonList(TestContext context) {
        Async async = context.async();

        this.dao.deleteAll();

        HttpClientRequest request = this.httpClient.get(8080, this.host, this.serverPath);
        request.exceptionHandler(err -> context.fail(err.getMessage()));

        request.handler(httpClientResponse -> {
            context.assertEquals(200, httpClientResponse.statusCode());

            httpClientResponse.handler(buffer -> {
                JsonArray jsonArray = new JsonArray(buffer.getString(0, buffer.length()));
                context.assertNotNull(jsonArray);
                context.assertEquals(0, jsonArray.size());

                async.complete();
            });
        });

        request.end();
    }

    @Test
    public void testEmptyCreateRequest(TestContext context) {
        Async async = context.async();

        HttpClientRequest request = this.httpClient.post(8080, this.host, this.serverPath);
        request.exceptionHandler(err -> context.fail(err.getMessage()));

        request.handler(httpClientResponse -> {
            context.assertEquals(400, httpClientResponse.statusCode());

            async.complete();
        });

        request.end();
    }

    @Test
    public void testEmptyCreateData(TestContext context) {
        Async async = context.async();

        String message = "{\"name\": \"\"}";
        HttpClientRequest request = this.httpClient.post(8080, this.host, this.serverPath);
        this.sendJsonRequest(context, request, message, httpClientResponse -> {
            context.assertEquals(422, httpClientResponse.statusCode());

            httpClientResponse.bodyHandler(buffer -> {
                JsonObject root = new JsonObject(buffer.getString(0, buffer.length()));
                context.assertNotNull(root);

                JsonObject errorObject = root.getJsonObject("errors");
                context.assertNotNull(errorObject);
                context.assertEquals(1, errorObject.size());

                JsonArray errorArray = errorObject.getJsonArray("name");
                context.assertNotNull(errorArray);
                context.assertEquals(1, errorArray.size());
                context.assertEquals("may not be empty", errorArray.getString(0));

                async.complete();
            });
        });
    }

    @Test
    public void testCreateToLongName(TestContext context) {
        Async async = context.async();

        String message = "{\"name\": \"lzcUomRUhfPrMo22CIet5MFhal4zF3HxKknEeyBcScmaZcnSPKY1JoELePSxwhFULRVTLvbPX3oSPiIye2WGqZ2WhJuoJEGDpCxjDWtHlE4fTjugwxTkfCn0m5r9a2mDjbXhC8Fu87WJRR7QgevlImBO3S3q4Apq6aOGp3DI28gVy8nxslrIHXDWDDblUUjKrWyu4MqVuRitamg4yl4w0TPKQ920fBjtUfsi1DGe8jC8Ks3GjcopGfFY9BqrFe8K\"}";
        HttpClientRequest request = this.httpClient.post(8080, this.host, this.serverPath);

        this.sendJsonRequest(context, request, message, httpClientResponse -> {
            context.assertEquals(422, httpClientResponse.statusCode());

            httpClientResponse.bodyHandler(buffer -> {
                JsonObject root = new JsonObject(buffer.getString(0, buffer.length()));
                context.assertNotNull(root);

                JsonObject errorObject = root.getJsonObject("errors");
                context.assertNotNull(errorObject);
                context.assertEquals(1, errorObject.size());

                JsonArray errorArray = errorObject.getJsonArray("name");
                context.assertNotNull(errorArray);
                context.assertEquals(1, errorArray.size());
                context.assertEquals("size must be between 0 and 255", errorArray.getString(0));

                async.complete();
            });
        });
    }

    @Test
    public void testCreateValid(TestContext context) {
        Async async = context.async();
        this.dao.deleteAll();

        String message = "{\"name\": \"Test1\"}";
        HttpClientRequest request = this.httpClient.post(8080, this.host, this.serverPath);

        this.sendJsonRequest(context, request, message, httpClientResponse -> {
            context.assertEquals(200, httpClientResponse.statusCode());

            httpClientResponse.bodyHandler(buffer -> {
                JsonObject entity = new JsonObject(buffer.getString(0, buffer.length()));
                context.assertNotNull(entity);
                context.assertEquals(3, entity.size());

                int entityId = entity.getInteger("id");
                context.assertNotEquals(0, entityId);

                String name = entity.getString("name");
                context.assertEquals("Test1", name);

                int referenceFileId = entity.getInteger("referenceFileId");
                context.assertEquals(0, referenceFileId);

                async.complete();
            });
        });
    }

    @Test
    public void testCreateNonUniqueProjectName(TestContext context) {
        Async async = context.async();
        this.dao.deleteAll();

        String message = "{\"name\": \"Test1\"}";
        HttpClientRequest request = this.httpClient.post(8080, this.host, this.serverPath);

        this.sendJsonRequest(context, request, message, httpClientResponse -> {
            context.assertEquals(200, httpClientResponse.statusCode());

            HttpClientRequest request2 = this.httpClient.post(8080, this.host, this.serverPath);
            this.sendJsonRequest(context, request2, message, httpClientResponse2 -> {
                context.assertEquals(422, httpClientResponse2.statusCode());

                httpClientResponse2.bodyHandler(buffer -> {
                    JsonObject root = new JsonObject(buffer.getString(0, buffer.length()));
                    context.assertNotNull(root);

                    JsonObject errorObject = root.getJsonObject("errors");
                    context.assertNotNull(errorObject);
                    context.assertEquals(1, errorObject.size());

                    JsonArray errorArray = errorObject.getJsonArray("name");
                    context.assertNotNull(errorArray);
                    context.assertEquals(1, errorArray.size());
                    context.assertEquals("must be unique", errorArray.getString(0));

                    async.complete();
                });
            });
        });
    }

    @Test
    public void updateInvalidEntity(TestContext context) {
        Async async1 = context.async();
        Async async2 = context.async();
        this.dao.deleteAll();

        HttpClientRequest request1 = this.httpClient.put(8080, this.host, this.serverPath + "/Invalid");
        String message = "{\"name\": \"Test1\"}";

        this.sendJsonRequest(context, request1, message, httpClientResponse -> {
            context.assertEquals(404, httpClientResponse.statusCode());

            async1.complete();
        });

        HttpClientRequest request2 = this.httpClient.put(8080, this.host, this.serverPath + "/1");

        this.sendJsonRequest(context, request2, message, httpClientResponse -> {
            context.assertEquals(404, httpClientResponse.statusCode());

            async2.complete();
        });
    }

    @Test
    public void testUpdateEmptyRequest(TestContext context) {
        Async async = context.async();
        this.dao.deleteAll();

        String message = "{\"name\": \"Test1\"}";
        HttpClientRequest request1 = this.httpClient.post(8080, this.host, this.serverPath);

        this.sendJsonRequest(context, request1, message, httpClientResponse1 -> {
            context.assertEquals(200, httpClientResponse1.statusCode());

            httpClientResponse1.bodyHandler(buffer -> {
                JsonObject entity = new JsonObject(buffer.getString(0, buffer.length()));
                int entityId = entity.getInteger("id");

                HttpClientRequest request2 = this.httpClient.put(8080, this.host, this.serverPath + "/" + entityId);
                this.sendJsonRequest(context, request2, "", httpClientResponse2 -> {
                    context.assertEquals(400, httpClientResponse2.statusCode());
                    async.complete();
                });
            });
        });
    }

    @Test
    public void testUpdateValid(TestContext context) {
        Async async = context.async();
        this.dao.deleteAll();

        String message1 = "{\"name\": \"Test1\"}";
        HttpClientRequest request1 = this.httpClient.post(8080, this.host, this.serverPath);

        this.sendJsonRequest(context, request1, message1, httpClientResponse1 -> {
            context.assertEquals(200, httpClientResponse1.statusCode());

            httpClientResponse1.bodyHandler(buffer1 -> {
                JsonObject entity1 = new JsonObject(buffer1.getString(0, buffer1.length()));
                int entityId = entity1.getInteger("id");
                String message2 = "{\"name\": \"Test2\"}";

                HttpClientRequest request2 = this.httpClient.put(8080, this.host, this.serverPath + "/" + entityId);

                this.sendJsonRequest(context, request2, message2, httpClientResponse2 -> {
                    context.assertEquals(200, httpClientResponse2.statusCode());

                    httpClientResponse2.bodyHandler(buffer2 -> {
                        JsonObject entity2 = new JsonObject(buffer2.getString(0, buffer2.length()));
                        context.assertNotNull(entity2);
                        context.assertEquals(3, entity2.size());

                        int newEntityId = entity2.getInteger("id");
                        context.assertEquals(entityId, newEntityId);

                        String name = entity2.getString("name");
                        context.assertEquals("Test2", name);

                        int referenceFileId = entity2.getInteger("referenceFileId");
                        context.assertEquals(0, referenceFileId);

                        async.complete();
                    });
                });
            });
        });
    }

    @Test
    public void testUpdateEmptyName(TestContext context) {
        Async async = context.async();
        this.dao.deleteAll();

        String message1 = "{\"name\": \"Test1\"}";
        HttpClientRequest request1 = this.httpClient.post(8080, this.host, this.serverPath);

        this.sendJsonRequest(context, request1, message1, httpClientResponse1 -> {
            context.assertEquals(200, httpClientResponse1.statusCode());

            httpClientResponse1.bodyHandler(buffer1 -> {
                JsonObject entity1 = new JsonObject(buffer1.getString(0, buffer1.length()));
                int entityId = entity1.getInteger("id");
                String message2 = "{\"name\": \"\"}";

                HttpClientRequest request2 = this.httpClient.put(8080, this.host, this.serverPath + "/" + entityId);

                this.sendJsonRequest(context, request2, message2, httpClientResponse2 -> {
                    context.assertEquals(422, httpClientResponse2.statusCode());

                    httpClientResponse2.bodyHandler(buffer2 -> {
                        JsonObject root = new JsonObject(buffer2.getString(0, buffer2.length()));
                        context.assertNotNull(root);

                        JsonObject errorObject = root.getJsonObject("errors");
                        context.assertNotNull(errorObject);
                        context.assertEquals(1, errorObject.size());

                        JsonArray errorArray = errorObject.getJsonArray("name");
                        context.assertNotNull(errorArray);
                        context.assertEquals(1, errorArray.size());
                        context.assertEquals("may not be empty", errorArray.getString(0));

                        async.complete();
                    });
                });
            });
        });
    }

    @Test
    public void testUpdateLongName(TestContext context) {
        Async async = context.async();
        this.dao.deleteAll();

        String message1 = "{\"name\": \"Test1\"}";
        HttpClientRequest request1 = this.httpClient.post(8080, this.host, this.serverPath);

        this.sendJsonRequest(context, request1, message1, httpClientResponse1 -> {
            context.assertEquals(200, httpClientResponse1.statusCode());

            httpClientResponse1.bodyHandler(buffer1 -> {
                JsonObject entity1 = new JsonObject(buffer1.getString(0, buffer1.length()));
                int entityId = entity1.getInteger("id");
                String message2 = "{\"name\": \"lzcUomRUhfPrMo22CIet5MFhal4zF3HxKknEeyBcScmaZcnSPKY1JoELePSxwhFULRVTLvbPX3oSPiIye2WGqZ2WhJuoJEGDpCxjDWtHlE4fTjugwxTkfCn0m5r9a2mDjbXhC8Fu87WJRR7QgevlImBO3S3q4Apq6aOGp3DI28gVy8nxslrIHXDWDDblUUjKrWyu4MqVuRitamg4yl4w0TPKQ920fBjtUfsi1DGe8jC8Ks3GjcopGfFY9BqrFe8K\"}";

                HttpClientRequest request2 = this.httpClient.put(8080, this.host, this.serverPath + "/" + entityId);

                this.sendJsonRequest(context, request2, message2, httpClientResponse2 -> {
                    context.assertEquals(422, httpClientResponse2.statusCode());

                    httpClientResponse2.bodyHandler(buffer2 -> {
                        JsonObject root = new JsonObject(buffer2.getString(0, buffer2.length()));
                        context.assertNotNull(root);

                        JsonObject errorObject = root.getJsonObject("errors");
                        context.assertNotNull(errorObject);
                        context.assertEquals(1, errorObject.size());

                        JsonArray errorArray = errorObject.getJsonArray("name");
                        context.assertNotNull(errorArray);
                        context.assertEquals(1, errorArray.size());
                        context.assertEquals("size must be between 0 and 255", errorArray.getString(0));

                        async.complete();
                    });
                });
            });
        });
    }

    @Test
    public void testDeleteInvalid(TestContext context) {
        Async async1 = context.async();
        Async async2 = context.async();
        this.dao.deleteAll();

        HttpClientRequest request1 = this.httpClient.delete(8080, this.host, this.serverPath + "/Test");

        this.sendJsonRequest(context, request1, "", httpClientResponse1 -> {
            context.assertEquals(404, httpClientResponse1.statusCode());
            async1.complete();
        });

        HttpClientRequest request2 = this.httpClient.delete(8080, this.host, this.serverPath + "/1");

        this.sendJsonRequest(context, request2, "", httpClientResponse2 -> {
            context.assertEquals(404, httpClientResponse2.statusCode());
            async2.complete();
        });
    }

    @Test
    public void testDeleteValid(TestContext context) {
        Async async = context.async();
        this.dao.deleteAll();

        RecordingProject p1 = new RecordingProject();
        p1.setName("Test1");

        RecordingProject p2 = new RecordingProject();
        p2.setName("Test2");

        p1.setId(this.dao.create(p1));
        p2.setId(this.dao.create(p2));

        HttpClientRequest request = this.httpClient.delete(8080, this.host, this.serverPath + "/" + p1.getId());

        this.sendJsonRequest(context, request, "", httpClientResponse -> {
            context.assertEquals(200, httpClientResponse.statusCode());
            List<RecordingProject> projectList = this.dao.getAll();

            context.assertEquals(1, projectList.size());
            context.assertEquals(p2.getId(), projectList.get(0).getId());

            async.complete();
        });
    }
}
