package com.example.hogwarts.hogwarts;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

import com.example.hogwarts.hogwarts.constant.User;

@SpringBootTest
class HogwartsApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private RedisTemplate redisTemplate;

	@Test
	public void testRedisTemplate() {
		System.out.println("RedisTemplate: " + redisTemplate);

		ValueOperations valueOperations = redisTemplate.opsForValue();
		HashOperations hashOperations = redisTemplate.opsForHash();
		ListOperations listOperations = redisTemplate.opsForList();
		SetOperations setOperations = redisTemplate.opsForSet();
		ZSetOperations zSetOperations = redisTemplate.opsForZSet();

	}

	@Test
	public void testRedisString() {
		// set get setex setnx
		redisTemplate.opsForValue().set("city", "Kuala Lumpur");
		String city = (String) redisTemplate.opsForValue().get("city");

		System.out.println("Redis String: " + city);

		redisTemplate.opsForValue().set("code", "1234", 30, TimeUnit.SECONDS);
		String code = (String) redisTemplate.opsForValue().get("code");

		System.out.println("Redis String Code: " + code);
	}

	@Test
	public void testHash() {
		// hset hget hdel hkeys hvals
		HashOperations hashOperations = redisTemplate.opsForHash();
		hashOperations.put("xiaoming", "name", "Jason");
		hashOperations.put("xiaoming", "age", "20");

		System.out.println("Redis hashset: " + hashOperations.get("xiaoming", "name"));
		System.out.println("Redis hashset: " + hashOperations.get("xiaoming", "age"));

		Set keys = hashOperations.keys("xiaoming");
		List values = hashOperations.values("xiaoming");

	}

	@Test
	public void testCommon() {
		Set keys = redisTemplate.keys("*");
		System.out.println("Keys: " + keys);

		System.out.println("Got: " + redisTemplate.hasKey("xiaoming"));

		for (Object key : keys) {
			DataType type = redisTemplate.type(key);
			System.out.println(type.name());
			// Retrieve value based on type
			Object value = null;
			switch (type) {
				case STRING:
					value = redisTemplate.opsForValue().get(key);
					break;
				case LIST:
					value = redisTemplate.opsForList().range(key, 0, -1); // Get full list
					break;
				case SET:
					value = redisTemplate.opsForSet().members(key); // Get all set members
					break;
				case ZSET:
					value = redisTemplate.opsForZSet().range(key, 0, -1); // Get sorted set values
					break;
				case HASH:
					value = redisTemplate.opsForHash().entries(key); // Get all hash fields and values
					break;
				default:
					value = "Unknown type or empty key";
			}
			System.out.println("Value: " + value);

		}
	}

	@Autowired
	private User user; // Inject the User bean

	@Test
	public void testConfigurationUser() {

		String name = user.getUsername();
		String age = user.getAge();
		System.out.println("NAME: " + name+ " " + age);
	}

}
